package com.stelpolvo.video.service;

import com.stelpolvo.video.dao.repository.UserInfoRepository;
import com.stelpolvo.video.dao.repository.VideoRepository;
import com.stelpolvo.video.domain.UserInfo;
import com.stelpolvo.video.domain.Video;
import com.stelpolvo.video.service.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final RestHighLevelClient restHighLevelClient;

    private final VideoRepository videoRepository;

    private final UserInfoRepository userInfoRepository;

    private final UserContextHolder userContext;

    public void addVideo(Video video) {
        video.setUserId(userContext.getCurrentUserId());
        videoRepository.save(video);
    }

    public void deleteVideo(Video video) {
        videoRepository.delete(video);
    }

    public void deleteAllVideos() {
        videoRepository.deleteAll();
    }

    public void updateVideo(Video video) {
        videoRepository.save(video);
    }

    public Video getVideo(String keyword) {
        return videoRepository.findByTitleLike(keyword);
    }

    public void addUserInfo(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }

    public List<Map<String, Object>> getContents(String keyword, Integer page, Integer size) throws IOException {
        String[] indices = {"videos", "user_infos"};
        SearchRequest searchRequest = new SearchRequest(indices);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(page - 1);
        sourceBuilder.size(size);
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, "title", "description", "username");
        sourceBuilder.query(multiMatchQueryBuilder);
        searchRequest.source(sourceBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // 设置高亮显示
        String[] fieldlist = {"title", "description", "username"};
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for (String s : fieldlist) {
            highlightBuilder.fields().add(new HighlightBuilder.Field(s));
        }
        // 多字段高亮情况需要设置为false
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);

        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : search.getHits()) {
            Map<String, HighlightField> map = hit.getHighlightFields();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            for (String s : fieldlist) {
                HighlightField highlightField = map.get(s);
                if (highlightField != null) {
                    Text[] fragments = highlightField.fragments();
                    String s1 = Arrays.toString(fragments);
                    String substring = s1.substring(1, s1.length() - 1);
                    sourceAsMap.put(s, substring);
                }
            }
            list.add(sourceAsMap);
        }
        return list;
    }
}
