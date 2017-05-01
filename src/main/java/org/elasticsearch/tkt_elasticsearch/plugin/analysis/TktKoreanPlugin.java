package org.elasticsearch.tkt_elasticsearch.plugin.analysis;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.tkt_elasticsearch.elasticsearch.index.analysis.TktKoreanTokenizerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wonseok on 2017. 2. 28..
 */

public class TktKoreanPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {

        Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> extraTokenizer = new HashMap<>();
        extraTokenizer.put("twitter_korean_tokenizer", new AnalysisModule.AnalysisProvider<TokenizerFactory>() {
            @Override
            public TokenizerFactory get(IndexSettings indexSettings, Environment environment, String name, Settings settings) throws IOException {
                return new TktKoreanTokenizerFactory(indexSettings, environment, name, settings);
            }
        });

        return extraTokenizer;
    }
}
