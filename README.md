# Android With Linux Server
My Practice Project

개인 공부를 위한 리눅스 서버 기반의 안드로이드 프로그램  
- Apache2, Mysql, PHP, Android Studio 사용  
- 개발언어 Java 사용  
- Retrofit2, GLIDE, RxJava, Jsoup, DataBinding 사용  

- 2022/01/10 Local 리눅스 서버 구축(VirtualBox) 및 DB Setting, PHP를 사용한 GET 구현(사용자 목록)  
![1](https://user-images.githubusercontent.com/97011241/148894536-4278cb30-8635-439c-b802-d26ecce9388f.png)

- 2022/01/11 PHP를 사용한 POST 구현  
![2](https://user-images.githubusercontent.com/97011241/148894613-0a99a44f-f05e-4b6d-be41-2eba82c513aa.png)  

- 2022/01/13 PHP를 사용한 PUT 구현, POST시 JSON을 Response 받아 내용에 따라서 앱에서 다른 분기 실행  
![1](https://user-images.githubusercontent.com/97011241/149296577-dfbc20a6-7a93-4949-a223-7091157f13ee.png)
![2](https://user-images.githubusercontent.com/97011241/149296586-414e208b-6d72-430b-9748-17265ff4a1da.png)  

- 2022/01/16 PHP를 사용한 DELETE 구현, PHP 파일 구조 수정(include 사용하여 DB 연결)
![1](https://user-images.githubusercontent.com/97011241/149626653-839d0cd1-c8ca-4838-85f5-94a834838826.png)  
![2](https://user-images.githubusercontent.com/97011241/149626655-9d40b84a-55d7-4ce5-a5d5-15e2bbfe12f2.png)  

- 2022/01/17 PHP를 사용한 서버 이미지 업로드 및 GLIDE를 사용한 서버 이미지 불러오기
![1](https://user-images.githubusercontent.com/97011241/149666054-87427874-c5b7-45b2-bbc9-2e00ed5729c7.png)  
![2](https://user-images.githubusercontent.com/97011241/149666056-d4978914-6da1-4692-b057-be144cee8405.png)  
![3](https://user-images.githubusercontent.com/97011241/149666058-87696a59-e8c3-4048-b627-64b680b6909c.png)  

- 2022/01/18 로그인ㆍ회원가입 기능 구현, UI 수정, PHP 불필요 내용 삭제 및 내용 수정, 회원정보를 불러와 해당하는 정보를 모두 표시  
![1](https://user-images.githubusercontent.com/97011241/149939710-148fc859-23b5-49af-9dbd-711f5bb684df.png)  
![2](https://user-images.githubusercontent.com/97011241/149939716-2873258e-ab8c-4f84-b5f3-f6cc2ca24fa2.png)  

- 2022/01/21 Main Activity를 Fragment로 변환 후 세가지(기존 Main Activity, Home, Dummy) Fragment를 이동할 수 있게함  
- 기존 Main Activity의 기능은 모두 정상 작동함  
![1](https://user-images.githubusercontent.com/97011241/150494113-fa265486-1352-48f4-b7b2-28e37ae6033b.png)  
  
- 2022/01/24 기존 코드에 Retrofit에서 사용되는 Enque 대신 Rxjava의 Single을 사용하도록 변경, 기존 Enque코드와 Retrofit Api Interface는 주석처리함  
  
- 2022/01/26 로그인시 SavePreference를 이용, 회원 정보저장에 체크하고 로그인 후 다시 앱을 실행하면 저장된 정보를 불러와 로그인 정보를 기입함  
  
- 2022/01/28 서버에 phpinfo를 불러와서 몇개의 정보를 파싱하여 홈 화면에 띄워줌, Jsoup 사용  
![1](https://user-images.githubusercontent.com/97011241/151493107-681b022f-3293-4d03-9f9d-485bee2f4616.png)  
  
- 2022/02/03 Rxjava의 clear 메소드를 이용하여 메모리 누수 방지, View Model을 사용하여 Fragment간 데이터 전달, Fragment에 Data Binding을 사용
![1](https://user-images.githubusercontent.com/97011241/152279726-5715c7d0-a0d8-44d2-84eb-1113cc6524e7.png)  
  
- 2022/02/11 Databinding을 더 많은 Activity에 적용, Android에서 Deprecated된 메소드들을 수정함, Fragment의 불필요한 로딩(replace 방식)을 개선하여 불필요한 로딩 최소화  
  
- 2022/02/14 화면 회전시 다른 Layout이 표시되도록 변경, 화면이 회전되서 on Destroy가 호출되어도 사용자가 마지막에 위치한 Fragment를 유지, View Model을 사용하여 서버와 통신한 데이터를 저장하고 on Destroy 호출 후 Activity 재 생성시 불필요한 서버 통신을 최소화
![1](https://user-images.githubusercontent.com/97011241/153829573-5a7bbd97-053b-47ee-8a2f-a5a662c002fd.png)  
  
- 2022/02/16 Fragment에 ViewPager2를 사용하여 화면 슬라이드로 Fragment 전환 가능하게 변경, 기존 Bottom Navigation에 있던 색상 설정 오류를 해결  
![Animation](https://user-images.githubusercontent.com/97011241/154844396-ef182720-d136-4921-8d12-3bcc46d7aee7.gif)
  
- 2022/02/18 안드로이드 스튜디오의 Profiler와 Leak Canary를 활용하여 메모리 누수 확인, 안드로이드 스튜디오에서 발견한 문제들을 확인하여 수정  

- 2022/02/28 기존에 로그인 정보 저장 기능을 위해 사용한 SavePreference를 대체하여 Room을 사용
![1](https://user-images.githubusercontent.com/97011241/155942571-9f650d92-89c7-432f-b8c7-315cef341b9b.png)  

- 2022/03/12 다른 프로젝트를 진행하면서 발견한 불필요 코드 
