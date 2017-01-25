# **Naverspeech SDK for Android**

> 본 저장소는 Android용 네이버 음성인식 라이브러리 및 예제 프로젝트를 포함하고 있습니다.

자세한 내용은 [네이버개발자 음성인식 API 명세](https://developers.naver.com/docs/labs/vrecog) 및 [Android API Document](http://naver.github.io/naverspeech-sdk-android/) 를 참고하세요.


v.1.1.0
-------------
### "이번 업데이트에서 새롭게 추가된 내용은 다음과 같습니다."
#### 1. [iOS 버전 openAPI](https://github.com/naver/naverspeech-sdk-ios) 출시
iOS 개발자분들을 위해 iOS 버전 openAPI를 새롭게 출시하였습니다.
끝점 추출(Endpoint detection), 안정화 등 이번에 추가된 새 업데이트가 모두 적용된 버전입니다. 많은 사용 부탁드립니다.

#### 2. 끝점 추출(Endpoint detection) 방식 추가
기존에는 발성을 멈추면 자동으로 인식 결과를 반환해주었습니다. 이는 발성의 끝점 추출(Endpoint detection)을 서버에서 자동으로 수행하는 방식입니다. 이번 업데이트에서는, 끝점 추출 동작에 대해 두 가지 방식을 새롭게 추가했습니다. 아래는 기존 방식을 포함한 세 가지 방식에 대한 설명이며, 자세한 사용 방법은 [Android 예제코드](https://github.com/naver/naverspeech-sdk-android) 또는 [iOS 예제코드](https://github.com/naver/naverspeech-sdk-ios)를 참고하시기 바랍니다.
  * Auto : 기존과 같은 방식입니다. 발성을 멈추면, 자동으로 인식 결과를 반환해줍니다.
  * Manual : 발성의 끝점을 사용자가 명시적으로 알립니다. 따라서 발성을 잠시 멈추어도 인식이 끝나지 않습니다. 이 방식을 이용하여 무전기의 push-to-talk과 유사한 동작을 구현할 수 있습니다.
  * Hybrid : 위의 Auto와 Manual 중 어떤 방식을 선택할 것인지는 빌드 타임에, 즉 코드상에서 개발자가 값을 넣어줌으로써 결정됩니다. 하지만 Hybrid 방식을 선택하면 이를 런타임에 결정할 수 있습니다. 가령, 버튼을 짧게 클릭하면 Auto 방식으로, 길게 누른 상태에서 발성하면 Manual 방식으로 다이나믹하게 결정하도록 구현할 수 있습니다.

#### 3. 중국어, 일본어 추가
이번 업데이트에서 새롭게 중국어, 일본어를 추가하였습니다.

#### 4. 열악한 환경에서의 안정화
네트워크 환경이 열악할 경우에도 음성인식이 보다 안정적으로 동작하도록, 내부적으로 오디오 버퍼링을 비롯한 여러 안정화 로직을 추가했습니다.
또한, 다양한 예외 상황에서 라이브러리가 안정적으로 동작하도록 내부적으로 많은 개선을 하였습니다. 그리고 IPv6 device에서 동작 가능하도록 수정했습니다.

#### 5. Android 버전 openAPI 사용성 개선
기존에는 libs 파일을 복사 & 붙여넣기하여 사용했지만, 이제부터는 android studio에서 gradle에 의존성을 추가하여 바로 SDK를 사용할 수 있도록 수정했습니다. 또한 사용성 개선을 위해 일부 메서드명과 형식을 변경하였습니다.
  * SpeechRecognizer constructor : SpeechConfig argument 제외
  * recognize : SpeechConfig argument 추가, return type 변경(→ boolean)
  * stopImmediately : 메서드명 변경(→ cancel), return type 변경(→ boolean)
  * stop : return type 변경(→ boolean)

Usage
-------------
**1.** app/build.gradle 파일에 아래 구문을 추가해주세요.
```
  repositories {
    jcenter()
  }
  dependencies {
    compile 'com.naver.speech.clientapi:naverspeech-sdk-android:1.1.2'
  }
```
**2.** proguard-rules.pro 파일에 아래의 구문을 추가해주세요. 애플리케이션이 보다 가볍고 안전해집니다.
```
  -keep class com.naver.speech.clientapi.SpeechRecognizer {
    protected private *;
  }
```
**3.** 아래와 같이 SpeechRecognitionListener를 상속(implements) 받아서 각 이벤트 발생 시 호출되는 콜백 메서드를 override하여 음성인식 기능을 구현합니다.

**4.** 자세한 내용은 [네이버개발자 음성인식 API 명세](https://developers.naver.com/docs/labs/vrecog) 및 [Android API Document](http://naver.github.io/naverspeech-sdk-android/) 를 참고하세요.


License
==

See [LICENSE](LICENSE) for full license text.

Copyright 2016 Naver Corp.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
