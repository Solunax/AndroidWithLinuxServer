# Android With Linux Server
My Practice Project

개인 공부를 위한 리눅스 서버 기반의 안드로이드 프로그램  
- Apache2, Mysql, PHP, Android Studio 사용  
- Java 사용  
- Retrofit2, GLIDE 사용  

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

- 2022/01/19 DB에 Query를 사용하는 함수들은 스레드로 처리하도록 변경, 핸들러를 통해 메인스레드의 UI 수정
