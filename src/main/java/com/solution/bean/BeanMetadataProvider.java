package com.solution.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.metadata.MetadataProvider;
import qunar.metadata.bean.extract.MetaDataExtract;

import java.util.Map;

/**
 * @author: hongyihui
 * @date: 2021/9/24 下午5:53
 */
public class BeanMetadataProvider implements MetadataProvider {

    private static final Logger log = LoggerFactory.getLogger(BeanMetadataProvider.class);

    @Override
    public String getType() {
        return "bean-map";
    }

    @Override
    public Object getMetadata(Map<String, Object> params) {
        return MetaDataExtract.getBeanMap();
    }
}