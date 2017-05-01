package org.elasticsearch.tkt_elasticsearch.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.tkt_elasticsearch.lucene.tokenizer.TktKoreanTokenizer;

import java.io.Reader;

/**
 * Created by wonseok on 2017. 2. 28..
 */
public class TktKoreanTokenizerFactory extends AbstractTokenizerFactory {
    /** whether to normalize text before tokenization. */
    private boolean enableNormalize = false;
    /** whether to stem text before tokenization. */
    private boolean enableStemmer = false;
    /** whtere to enable phrase parsing. */
    private boolean enablePhrase = false;

    public TktKoreanTokenizerFactory(
            IndexSettings indexSettings, Environment env,
            String name, Settings settings) {
        super(indexSettings, name, settings);

        this.enableNormalize = settings.getAsBoolean("enableNormalize", false);
        this.enableStemmer = settings.getAsBoolean("enableStemmer", false);

    }

    @Override
    public Tokenizer create() {
        Tokenizer tokenizer = new TktKoreanTokenizer(this.enableNormalize, this.enableStemmer, this.enablePhrase);
        return tokenizer;
    }
}
