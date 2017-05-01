package org.elasticsearch.tkt_elasticsearch.lucene.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;

import com.twitter.penguin.korean.KoreanPosJava;
import com.twitter.penguin.korean.KoreanTokenJava;
import com.twitter.penguin.korean.TwitterKoreanProcessorJava;
import com.twitter.penguin.korean.phrase_extractor.KoreanPhraseExtractor.KoreanPhrase;
import com.twitter.penguin.korean.tokenizer.KoreanTokenizer.KoreanToken;

import scala.collection.Seq;

public class TktKoreanTokenizer extends Tokenizer{
	/** whether input stream was read as a string. */
	private boolean isInputRead = false;
	/** current index of token buffers. */
	private int tokenIndex = 0;
	/** token buffers. */
	List<KoreanTokenJava> tokenBuffer = null;

	/** whether to normalize text before tokenization. */
	private boolean enableNormalize = true;
	/** whether to stem text before tokenization. */
	private boolean enableStemmer = true;
	/** whtere to enable phrase parsing. */
	private boolean enablePhrase = false;

	private CharTermAttribute charTermAttribute = null;
	private OffsetAttribute offsetAttribute = null;
	private TypeAttribute typeAttribute = null;
	
	public TktKoreanTokenizer() {
		this(false, false, false);
	}


	/**
	 * Constructor
	 *
	 */
	public TktKoreanTokenizer(boolean enableNormalize, boolean enableStemmer, boolean enablePhrase) {
		super(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);
		this.enableNormalize = enableNormalize;
		this.enableStemmer = enableStemmer;
		this.enablePhrase = enablePhrase;
		
		initAttributes();
	}

	
	@Override
	public boolean incrementToken() throws IOException {
		clearAttributes();

		if (this.isInputRead == false) {
//			System.out.println("inc: " + this.enableNormalize);
//			System.out.println("inc: " + this.enableStemmer);
			
			
			this.isInputRead = true;
			CharSequence text = readText();
			Seq<KoreanToken> tokens = TwitterKoreanProcessorJava.tokenize(text);
			
			if ( this.enableStemmer ) {
				tokens  = TwitterKoreanProcessorJava.stem(tokens);
			}
			
			if ( this.enablePhrase ) {
				List<KoreanPhrase> phrases = TwitterKoreanProcessorJava.extractPhrases(tokens, true, true);
				this.tokenBuffer = new ArrayList<KoreanTokenJava>();
				for (KoreanPhrase phrase : phrases ) {
					this.tokenBuffer.add(new KoreanTokenJava(phrase.text(), KoreanPosJava.valueOf(phrase.pos().toString()), phrase.offset(), phrase.length(), false));
				}
				
			} else {
				this.tokenBuffer = TwitterKoreanProcessorJava.tokensToJavaKoreanTokenList(tokens);
			}
		}
		
		if (this.tokenBuffer == null || this.tokenBuffer.isEmpty() || tokenIndex >= this.tokenBuffer.size() ) {
			return false;
		}

		setAttributes(this.tokenBuffer.get(tokenIndex++));

		return true;
	}
	/**
	 * Add attributes
	 * 
	 */
	private void initAttributes() {
		this.charTermAttribute = addAttribute(CharTermAttribute.class);
		this.offsetAttribute = addAttribute(OffsetAttribute.class);
		this.typeAttribute = addAttribute(TypeAttribute.class);
	}

	
	/**
	 * Set attributes
	 * 
	 * @param token
	 */
	private void setAttributes(KoreanTokenJava token) {
		charTermAttribute.append(token.getText());
		offsetAttribute.setOffset(token.getOffset(), token.getOffset() + token.getLength());
		typeAttribute.setType(token.getPos().toString());
		
	}

	/**
	 * Read string from input reader.
	 * 
	 * @return
	 * @throws IOException
	 */
	private CharSequence readText() throws IOException {
		StringBuilder text = new StringBuilder();
		char[] tmp = new char[1024];
		int len = -1;
		while ((len = input.read(tmp)) != -1) {
			text.append(new String(tmp, 0, len));
		}
		
		if ( this.enableNormalize ) {
			return TwitterKoreanProcessorJava.normalize(text.toString());
		} else {
			return text.toString();
		}
		
	}

	/**
	 * Initailze states.
	 */
	private void initializeState() {
		this.isInputRead = false;
		this.tokenIndex = 0;
		this.tokenBuffer = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.lucene.analysis.Tokenizer#close()
	 */
	@Override
	public void close() throws IOException {
		super.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.lucene.analysis.Tokenizer#reset()
	 */
	@Override
	public void reset() throws IOException {
		super.reset();

		initializeState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.lucene.analysis.TokenStream#end()
	 */
	@Override
	public void end() throws IOException {
		super.end();
	}
}
