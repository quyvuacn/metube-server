package org.aptech.metube.videoservice.service.impl;

import org.aptech.metube.videoservice.config.RestTemplateConfig;
import org.aptech.metube.videoservice.config.Translator;
import org.aptech.metube.videoservice.constant.EntityStatusCode;
import org.aptech.metube.videoservice.controller.request.UpdateVideoStatusRequest;
import org.aptech.metube.videoservice.controller.request.VideoCreateRequest;
import org.aptech.metube.videoservice.controller.response.ApiResponse;
import org.aptech.metube.videoservice.controller.response.UserAccountTypeResponse;
import org.aptech.metube.videoservice.controller.response.VideoResponse;
import org.aptech.metube.videoservice.dto.VideoDto;
import org.aptech.metube.videoservice.entity.Category;
import org.aptech.metube.videoservice.entity.Video;
import org.aptech.metube.videoservice.exception.NotFoundException;
import org.aptech.metube.videoservice.exception.RequestValidException;
import org.aptech.metube.videoservice.mapper.VideoMapper;
import org.aptech.metube.videoservice.repo.CategoryRepository;
import org.aptech.metube.videoservice.repo.VideoRepository;
import org.aptech.metube.videoservice.security.utils.JwtUtils;
import org.aptech.metube.videoservice.service.VideoService;
//import com.google.cloud.ReadChannel;
//import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.BlobId;
//import com.google.cloud.storage.Storage;
import org.aptech.metube.videoservice.specification.VideoSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@Service
public class VideoServiceImpl implements VideoService {
    private final Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);
    @Value("${gcp.bucket.name}")
    private String bucketName;
    @Value("${base.url}")
    private String apiBaseUrl;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    VideoMapper mapper;
//    @Autowired
//    Storage storage;
    @Autowired
    VideoRepository repository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    RedisTemplate redisTemplate;
    private static final int MAX_CACHE_SIZE = 30;

    @Override
    public void deleteVideo(Long id) {
        Video video = repository.getById(id);
        video.setIsDeleted(true);
        repository.save(video);
    }
    public List<VideoResponse> getListVideo(String search, Integer pageNum, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNum, pageSize);
        Page<Video> listVideo = repository.findAll(
                Specification.where(VideoSpecification.hasTitle(search).and(VideoSpecification.hasIsAds(false).and(VideoSpecification.hasStatusActive(EntityStatusCode.valueOf("ACTIVE"))))), paging);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")){
            return listVideo.getContent().stream().map(
                    video -> {
                        VideoResponse videoResponse = VideoResponse.builder()
                                .id(video.getId())
                                .description(video.getDescription())
                                .title(video.getTitle())
                                .isAds(video.getIsAds())
                                .videoUrl(video.getVideoUrl())
                                .thumbnail(video.getThumbnail())
                                .userId(video.getUserId())
                                .countDislike(video.getCountDislike())
                                .countLike(video.getCountLike())
                                .countView(video.getCountView())
                                .categories(video.getCategories())
                                .createdDate(video.getCreatedDate())
                                .build();
                        return videoResponse;
                    }
            ).toList();
        }
        UserDetailsImpl userDetails = getCurrentUser();
        Long userId = Long.valueOf(userDetails.getId());
        if (search == null || search.isEmpty()){
            return listVideo.getContent().stream().map(
                    video -> {
                        VideoResponse videoResponse = VideoResponse.builder()
                                .id(video.getId())
                                .description(video.getDescription())
                                .title(video.getTitle())
                                .isAds(video.getIsAds())
                                .videoUrl(video.getVideoUrl())
                                .thumbnail(video.getThumbnail())
                                .userId(video.getUserId())
                                .countDislike(video.getCountDislike())
                                .countLike(video.getCountLike())
                                .countView(video.getCountView())
                                .categories(video.getCategories())
                                .createdDate(video.getCreatedDate())
                                .build();
                        return videoResponse;
                    }
            ).toList();
        }
        cacheSearchString(userId, search.trim());
        return listVideo.getContent().stream().map(
                video -> {
                    VideoResponse videoResponse = VideoResponse.builder()
                            .id(video.getId())
                            .description(video.getDescription())
                            .title(video.getTitle())
                            .isAds(video.getIsAds())
                            .videoUrl(video.getVideoUrl())
                            .thumbnail(video.getThumbnail())
                            .userId(video.getUserId())
                            .countDislike(video.getCountDislike())
                            .countLike(video.getCountLike())
                            .countView(video.getCountView())
                            .categories(video.getCategories())
                            .createdDate(video.getCreatedDate())
                            .build();
                    return videoResponse;
                }
        ).toList();
    }
    public List<String> searchCacheContains(String searchString) {
        UserDetailsImpl userDetails = getCurrentUser();
        long userId = userDetails.getId();

        String cacheKey = "user:" + userId + ":searchHistory";

        List<String> userSearchHistory = redisTemplate.opsForList().range(cacheKey, 0, -1);

        if (userSearchHistory == null) {
            return Collections.emptyList();
        }
        if (searchString == null || searchString.isEmpty()){
            return userSearchHistory;
        }
        List<String> matchingSearches = userSearchHistory
                .stream()
                .filter(search -> search.toUpperCase().contains(searchString.trim().toUpperCase()))
                .collect(Collectors.toList());
        return matchingSearches;
    }

    public void countView(Long videoId){
        Video video;
        try {
            video = repository.findById(videoId)
                    .orElseThrow(() -> new NotFoundException(Translator.toLocale("not.found.video")));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        if (video.getCountView() > 0){
            video.setCountView(video.getCountView()+1);
        } else {
            video.setCountView(1);
        }
        repository.save(video);
    }

    private void cacheSearchString(long userId, String search) {
        String cacheKey = "user:" + userId + ":searchHistory";

        List<String> userSearchHistory = redisTemplate.opsForList().range(cacheKey, 0, -1);
        if (userSearchHistory.stream().noneMatch(s -> s.trim().equals(search.trim()))){
            redisTemplate.opsForList().leftPush(cacheKey, search);

            if (userSearchHistory != null && userSearchHistory.size() > MAX_CACHE_SIZE) {
                redisTemplate.opsForList().trim(cacheKey, 0, MAX_CACHE_SIZE - 1);
            }
        }
    }

    @Override
    public List<VideoResponse> getVideoById(long id, HttpServletRequest request) throws NotFoundException {
        List<VideoResponse> responses = new ArrayList<>();
        Video video = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("not.found.video")));
        List<Category> categories = categoryRepository.findCategoriesByVideoId(video.getId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = getCurrentUser();
        Long userId = Long.valueOf(userDetails.getId());
        cacheRecommendation(userId, categories);
        VideoResponse videoResponse = VideoResponse.builder()
                .id(video.getId())
                .videoUrl(video.getVideoUrl())
                .countDislike(video.getCountDislike())
                .countLike(video.getCountLike())
                .countView(video.getCountView())
                .categories(categories)
                .description(video.getDescription())
                .isAds(video.getIsAds())
                .thumbnail(video.getThumbnail())
                .title(video.getTitle())
                .userId(video.getUserId())
                .createdDate(video.getCreatedDate())
                .build();

        Video videoAds = repository.getVideoByIsAdsIs(true);
        // check xem videoAds có null không, nếu null thì không add vào responses nữa
        if (Objects.isNull(videoAds)){
            responses.add(videoResponse);
            return responses;
        }
        VideoResponse videoAdsResponse = VideoResponse.builder()
                .id(videoAds.getId())
                .videoUrl(videoAds.getVideoUrl())
                .countDislike(videoAds.getCountDislike())
                .countLike(videoAds.getCountLike())
                .countView(videoAds.getCountView())
                .categories(categories)
                .description(videoAds.getDescription())
                .isAds(videoAds.getIsAds())
                .thumbnail(videoAds.getThumbnail())
                .title(videoAds.getTitle())
                .userId(videoAds.getUserId())
                .build();

        if (!authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")){
            responses.add(videoResponse);
            responses.add(videoAdsResponse);
            return responses;
        }
        List<UserAccountTypeResponse> response = getUserAccountTypeByUserId(userId, request);

        if (response.isEmpty()){
            responses.add(videoResponse);
            responses.add(videoAdsResponse);
            return responses;
        }
        UserAccountTypeResponse maxExpireDateUser = response.stream()
                .max((u1, u2) -> u1.getExpireDate().compareTo(u2.getExpireDate()))
                .orElse(null);
        LocalDateTime maxExpireDate = maxExpireDateUser.getExpireDate();
        if (response.size() == 1 && response.get(0).getExpireDate().isBefore(LocalDateTime.now())) {
            responses.add(videoResponse);
            responses.add(videoAdsResponse);
            return responses;
        } else if (response.size() == 1 && response.get(0).getExpireDate().isAfter(LocalDateTime.now())) {
            responses.add(videoResponse);
            return responses;
        }
        responses.add(videoResponse);

        return responses;
    }

    public List<UserAccountTypeResponse> getUserAccountTypeByUserId(Long userId, HttpServletRequest request) throws NotFoundException {
        String apiUrl = apiBaseUrl + "/api/v1/account-type/user/" + userId;
        String token = jwtUtils.extractTokenFromRequest(request);

        ParameterizedTypeReference<Integer> requestType = new ParameterizedTypeReference<Integer>() {};
        ParameterizedTypeReference<ArrayList<UserAccountTypeResponse>> responseType = new ParameterizedTypeReference<>() {};
        ArrayList<UserAccountTypeResponse> result = RestTemplateConfig.callApiMethodGET(apiUrl, token, requestType, responseType, restTemplate);

        return result;
    }
    public List<Video> getRecommendVideo() {
        List<Video> responses = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return responses;
        }
        UserDetailsImpl userDetails = getCurrentUser();
        Long userId = Long.valueOf(userDetails.getId());
        List<Long> categoryIds = getUserRecommendation(userId);

        if (categoryIds != null && !categoryIds.isEmpty()) {
            Map<Long, Long> categoryIdCount = categoryIds.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            List<Long> top3CategoryIds = categoryIdCount.entrySet().stream()
                    .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                    .limit(3)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            for (Long categoryId : top3CategoryIds) {
                List<Video> videos = repository.findVideosByCategoryId(categoryId);
                if (!CollectionUtils.isEmpty(videos)) {
                    videos = videos.stream().limit(5).collect(Collectors.toList());
                    responses.addAll(videos);
                }
            }
        }

        return responses;
    }



    public List<Long> getUserRecommendation(Long userId){
        String cacheKey = "user:" + userId + ":recommendation";
        List<Object> userRecommendation = redisTemplate.opsForList().range(cacheKey, 0, -1);
        return userRecommendation.stream()
                .map(id -> Long.parseLong(id.toString()))
                .collect(Collectors.toList());
    }


    private void cacheRecommendation(long userId, List<Category> categories) {
        String cacheKey = "user:" + userId + ":recommendation";

        List<Long> categoryIds = categories.stream()
                .map(Category::getId)
                .collect(Collectors.toList());

        List<?> userRecommendation = redisTemplate.opsForList().range(cacheKey, 0, -1);

        redisTemplate.opsForList().leftPushAll(cacheKey, categoryIds.toArray());
        if (userRecommendation != null && userRecommendation.size() > MAX_CACHE_SIZE) {
            redisTemplate.opsForList().trim(cacheKey, 0, MAX_CACHE_SIZE - 1);
        }
    }

    public UserDetailsImpl getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return (UserDetailsImpl) authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public Video updateVideo(VideoDto request) {
        Video video = repository.getById(request.getId());

        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setThumbnail(request.getThumbnail());
        video.setTitle(request.getTitle() != null ? request.getTitle() : video.getTitle());
        video.setDescription(request.getDescription() != null ? request.getDescription() : video.getDescription());
        video.setThumbnail(request.getThumbnail() != null ? request.getThumbnail() : video.getThumbnail());

        Video result = repository.save(video);
        return result;
    }

    @Override
    public Video saveVideoInfo(VideoCreateRequest request) throws NotFoundException {

        List<Category> categoryList = new ArrayList<>();
        for (Long id: request.getCategoryIds()){
            Category category = categoryRepository.findById(id)
                            .orElseThrow(()-> new NotFoundException(Translator.toLocale("category.not.found")));
            categoryList.add(category);
        }

        Video video = new Video();
        video.setIsAds(request.getIsAds() != null ? request.getIsAds() : false);
        video.setCountDislike(0);
        video.setCountLike(0);
        video.setUserId(Long.valueOf(getCurrentUser().getId()));
        video.setVideoUrl(request.getVideoUrl());
        video.setDescription(request.getDescription());
        video.setTitle(request.getTitle());
        video.setThumbnail(request.getThumbnail());
        video.setCategories(categoryList);
        repository.save(video);
        return video;
    }

//    public ResponseEntity<byte[]> prepareContent(final String fileName, final String fileType, final String range) {
//
//        try {
//            final String fileKey = fileName;
//            long rangeStart = 0;
//            long rangeEnd = VideoConstant.CHUNK_SIZE;
//            final Long fileSize = getFileSize(fileKey);
//            if (range == null) {
//                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
//                        .header(VideoConstant.CONTENT_TYPE, VideoConstant.VIDEO_CONTENT + fileType)
//                        .header(VideoConstant.ACCEPT_RANGES, VideoConstant.BYTES)
//                        .header(VideoConstant.CONTENT_LENGTH, String.valueOf(rangeEnd))
//                        .header(VideoConstant.CONTENT_RANGE, VideoConstant.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
//                        .header(VideoConstant.CONTENT_LENGTH, String.valueOf(fileSize))
//                        .body(readByteRangeNew(fileKey, rangeStart, rangeEnd)); // Read the object and convert it as bytes
//            }
//            String[] ranges = range.split("-");
//            rangeStart = Long.parseLong(ranges[0].substring(6));
//            if (ranges.length > 1) {
//                rangeEnd = Long.parseLong(ranges[1]);
//            } else {
//                rangeEnd = rangeStart + VideoConstant.CHUNK_SIZE;
//            }
//
//            rangeEnd = Math.min(rangeEnd, fileSize - 1);
//            final byte[] data = readByteRangeNew(fileKey, rangeStart, rangeEnd);
//            final String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
//            HttpStatus httpStatus = HttpStatus.PARTIAL_CONTENT;
//            if (rangeEnd >= fileSize) {
//                httpStatus = HttpStatus.OK;
//            }
//            return ResponseEntity.status(httpStatus)
//                    .header(VideoConstant.CONTENT_TYPE, VideoConstant.VIDEO_CONTENT + fileType)
//                    .header(VideoConstant.ACCEPT_RANGES, VideoConstant.BYTES)
//                    .header(VideoConstant.CONTENT_LENGTH, contentLength)
//                    .header(VideoConstant.CONTENT_RANGE, VideoConstant.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
//                    .body(data);
//        } catch (IOException e) {
//            logger.error("Exception while reading the file {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @Override
    public Map<String, Object> findAllByUserId(Long userId, Integer pageNum, Integer pageSize, HttpServletRequest request) throws NotFoundException {
        getUserInRequest(userId, request);
        Pageable paging = PageRequest.of(pageNum, pageSize);
        Page<Video> pageVideos = repository.findAllByUserId(userId, paging);
        List<Video> videos = new ArrayList<>();
        videos = pageVideos.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("videos", videos);
        response.put("currentPage", pageVideos.getNumber());
        response.put("totalItems", pageVideos.getTotalElements());
        response.put("totalPages", pageVideos.getTotalPages());
        return response;
    }

    @Override
    public VideoResponse getLatestVideoByUserId(Long userId, HttpServletRequest request) throws NotFoundException {
        getUserInRequest(userId, request);

        Video video = repository.getLatestVideoByUserId(userId);
        return VideoResponse.builder()
                .title(video.getTitle())
                .videoUrl(video.getVideoUrl())
                .isAds(video.getIsAds())
                .id(video.getId())
                .countView(video.getCountView())
                .countLike(video.getCountLike())
                .countDislike(video.getCountDislike())
                .userId(video.getUserId())
                .thumbnail(video.getThumbnail())
                .description(video.getDescription())
                .createdDate(video.getCreatedDate())
                .build();
    }

    @Override
    public List<VideoResponse> getVideosByCategoryId(Long categoryId) throws NotFoundException {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("category.not.found")));
        List<Video> videos = repository.findVideosByCategoryId(categoryId);
        return videos.stream().map(
                video -> {
                    VideoResponse videoResponse = VideoResponse.
                            builder()
                            .title(video.getTitle())
                            .videoUrl(video.getVideoUrl())
                            .isAds(video.getIsAds())
                            .id(video.getId())
                            .countView(video.getCountView())
                            .countLike(video.getCountLike())
                            .countDislike(video.getCountDislike())
                            .userId(video.getUserId())
                            .thumbnail(video.getThumbnail())
                            .description(video.getDescription())
                            .createdDate(video.getCreatedDate())
                            .build();
                    return videoResponse;
                })
                .toList();
    }

    private void getUserInRequest(Long userId, HttpServletRequest request) throws NotFoundException {
        String apiUrl = apiBaseUrl + "/api/v1/users/get/" + userId;
        String token = jwtUtils.extractTokenFromRequest(request);

        ParameterizedTypeReference<Integer> requestType = new ParameterizedTypeReference<Integer>() {};
        ParameterizedTypeReference<Map<String,Object>> responseType = new ParameterizedTypeReference<>() {};
        Map<String, Object> result = RestTemplateConfig.callApiMethodGET(apiUrl, token, requestType, responseType, restTemplate);
        assert result != null;
        if (result.get("id") == null){
            throw new NotFoundException(Translator.toLocale("user.not.found"));
        }
    }

//    public byte[] readByteRangeNew(String filename, long start, long end) throws IOException {
//        BlobId blobId = BlobId.of(bucketName, filename);
//        Blob blob = storage.get(blobId);
//
//        try (ReadChannel reader = blob.reader()) {
//            reader.seek(start);
//
//            int bufferSize = (int) (end - start) + 1;
//            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
//
//            int bytesRead = reader.read(buffer);
//            if (bytesRead != bufferSize) {
//                throw new IOException("Failed to read the requested range");
//            }
//
//            return buffer.array();
//        }
//    }
//
//    public Long getFileSize(String filename) {
//        BlobId blobId = BlobId.of(bucketName, filename);
//        Blob blob = storage.get(blobId);
//        return blob.getSize();
//    }
    @Override
    public ApiResponse updateVideoStatus(UpdateVideoStatusRequest request){
        if (request.getUserId() == null || request.getStatusCode() == null){
            throw new RequestValidException(Translator.toLocale("invalid.param"));
        }
        List<Video> videos = repository.findVideosByUserId(request.getUserId());
        if (videos != null){
            for (Video video : videos){
                video.setStatusCode(request.getStatusCode());
                repository.save(video);
            }
            return new ApiResponse(OK, Translator.toLocale("Success"), true);
        }
        return new ApiResponse(OK, Translator.toLocale("user.has.no-video"), false);
    }
}
