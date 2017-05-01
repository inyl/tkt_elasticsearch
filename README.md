# tkt_elasticsearch

twitter korean text elasticsesarch plugin


[twitter korean text](https://github.com/twitter/twitter-korean-text)를 elasticsearch에서 쓸 수 있게 만든 플러그인입니다.


기존에 존재하던 [tkt_elasticsearch](https://github.com/socurites/tkt-elasticsearch)
는 구버전에만 호환되어 elasticsearch 최신버전과 호환되게 수정하였습니다.

호환표와 버전은 다음을 참조 해주세요

| plugin-version | elasticsearch | twitter korean text | java | install path                                                                 |
|----------------|---------------|---------------------|------|------------------------------------------------------------------------------|
| 0.0.1          | 5.3.0         | 4.4                 | 1.7  | https://github.com/inyl/tkt_elasticsearch/raw/master/build/elasticsearch.zip |
|                |               |                     |      |                                                                              |
|                |               |                     |      |                                                                              |


## 설치방법

```shell
cd {elasticsearch directory}/bin
./elasticsearch-plugin install https://github.com/inyl/tkt_elasticsearch/raw/master/build/elasticsearch.zip
```

## 테스트
```shell
PUT http://localhost:9200/test/
{
    "index" : {
        "analysis" : {
            "analyzer" : {
                "korean" : {
                    "tokenizer" : "twitter_korean_tokenizer"
                }
            },
            "tokenizer": {
              "twitter_korean_tokenizer" : {
                "type": "twitter_korean_tokenizer",
                "enableNormalize": false,
                "enableStemmer": false
              }
            }
        }
    }
}

```

한글 테스트
```shell
curl -XGET -H "application/json; charset=UTF-8" "http://localhost:9200/test/_analyze?analyzer=korean&text=%EB%B0%94%EC%81%9C%EB%B2%8C%EA%BF%80%EC%9D%80%EC%8A%AC%ED%8D%BC%ED%95%A0%EC%8B%9C%EA%B0%84%EB%8F%84%EC%97%86%EB%8B%A4"
```
```json
{
	"tokens": [
		{
			"token": "바쁜",
			"start_offset": 0,
			"end_offset": 2,
			"type": "Adjective",
			"position": 0
		},
		{
			"token": "벌꿀",
			"start_offset": 2,
			"end_offset": 4,
			"type": "ProperNoun",
			"position": 1
		},
		{
			"token": "은",
			"start_offset": 4,
			"end_offset": 5,
			"type": "Josa",
			"position": 2
		},
		{
			"token": "슬퍼할",
			"start_offset": 5,
			"end_offset": 8,
			"type": "Verb",
			"position": 3
		},
		{
			"token": "시간",
			"start_offset": 8,
			"end_offset": 10,
			"type": "Noun",
			"position": 4
		},
		{
			"token": "도",
			"start_offset": 10,
			"end_offset": 11,
			"type": "Josa",
			"position": 5
		},
		{
			"token": "없다",
			"start_offset": 11,
			"end_offset": 13,
			"type": "Adjective",
			"position": 6
		}
	]
}
```
<작성중>

## 버전 변경방법 (rebuild)
elasticsearch가 플러그인이 버전이 완전히 매칭되지 않으면 플러그인이 설치되지 않게 변경이 되었습니다.<br/>
따라서 이 프로젝트가 모든 버전을 지원해드리기는 어려운 상황입니다.<br/>
왠만한 5버전대는 property파일만 수정해서 버전을 맞출 수 있습니다. 다음은 버전수정 가이드라인입니다.
<<작성중>>