# StudyingKotlin
2021-01-10 start
열심히 코틀린 공부해보겠습니다ㅏㅏ
 
### Pra01_LottoNumber
자동 로또 번호 생성기 앱 [2021-01-10]
* 핵심
  * Random
  * Shuffle
  * HashCode

### Pra02_QuizLocker
퀴즈 잠금화면 앱 [2021-01-11]
* 핵심
  * BroadcastReceiver - 화면이 꺼졌을 때의 이벤트
  * Service - 앱이 종료되어도 실행하기 위해 **(UI없이 백그라운드에서 실행 )**
  * file, shared preference, PreferenceFragment
  * SeekBar
  * 나인 패치
  * Vibration

### Pra03_PunchPower
펀치력 측정 앱 [2021-01-13]
* 핵심
  * Sensor
  * Animation
  
 ### Pra04_SeoulToilet
 서울시 화장실 검색 앱 [2021-01-13]
 * 핵심
   * Google Maps
   * CardView
   * FloatingActionButton
   * 쓰레드 **(네트워크 작업은 긴시간이 필요하여 UI 쓰레도를 사용하면 UI가 멈춰버리기 때문)**
   * Google Maps ClusterItem (interface) (**ClusterManager가 화면에 표시할 정보**를 제공하는 인터페이스)
   * Google Maps ClusterManager (구글맵과 ClusterItem으로 맵에 **'마커'** 로 표시할지 **'숫자로 된 원'** 으로 표시할지 관리하는 클래스)
   * Google Maps ClusterRenderer (마커의 아이콘을 바꿈)
   * AutoCompleteText (텍스트 자동완성)
   
### Pra05_AnonymousSNS
익명 소셜 서비스 앱 [2021-01-15]
* 핵심
  * RecyclerView
  * Picasso 이미지 로드 라이브러리 (메모리의 효율적인 관리를 위해)
  * Firebase
