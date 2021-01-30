# HighSchoolAllTime
동작 결과 실행 동영상
-https://www.youtube.com/watch?v=3kfZk2Mhfzk&feature=youtu.be

1. 팀 활동 시 규칙 및 팀원간 역할 분담, 기본적 주간 계획표 작성

2. 팀원 간 회의 진행
	-아이디어 제시 및 현재 실사용 되고 있는 아이디어에 관련된 어플 조사
	1안) 아이클립. 숨어있는 카드 포인트 확인 어플
	2안) 삼성 헬스케어. 건강 컨텐츠 어플로써 걸음 수 및 활동 시간, 운동 종류, 신단 조절 등 사용자 운동량 체크
	3안) 에브리타임. 대학생활 커뮤니티 어플로 시간표 및 익명 커뮤니티 게시판(새내기, 장터, 스터디, 동아리, 학생회 등), 학식 등 정보 제공 및 커뮤니케이션 어플
	-회의를 통한 어플 설계 주제 선정
	: 대학생을 대상으로한 커뮤니티인 에브리타임의 어플을 참고하여 고등학생을 대상으로 커뮤니티 어플을 설계

3. 주제에 맞는 어플의 각 화면 스케치 및 담당 화면 분담
	어플 구동 시 필요한 화면을 스케치함(로그인, 시간표, 급식표, 게시판, My Page 등)

4. 개별 분담한 화면의 기초 틀 Android Studio에서 실제 구현
	로그인 : [담당자 최강현] 어플로고로 사용 할 ImageView와 아이디 및 패스워드를 입력 할 2개의 EditText, 로그인 및 회원가입, 아이디/패스워드 찾기 버튼으로 구성
	회원가입 : [담당자 최강현] 화면 Tittle인 TextView, 필요 정보(학교 명, 아이디, 패스워드, 이름, 이메일, 교직)를 입력 할 EditText와 확인 버튼과 아이디의 중복값을 없애기 위한 중복확인 버튼으로 구성
	아이디 패스워드 찾기 : [담당자 이시형] 아이디 찾기(이름, 이메일), 패스워드 찾기(이름, 이메일, 아이디) 시 필요 정보를 입력 할 EditText와 확인 버튼으로 구성
	New 패스워드 설정 : [담당자 이시형] 패스워드 찾기 기능에서 필요 정보 입력 후 확인 버튼 클릭 시 보여줄 화면으로 새로운 패스워드를 입력해 Update할 화면. 2번에 걸쳐 확인 후 Update할 예정으로 2개의 EditText와 확인 버튼으로 구성
	My Page : [담당자 이시형] 사용자 정보를 보여주는 화면으로 사용자가 작성한 게시글 및 댓글을 보여주는 ListView와 이름(TextView), 로그아웃 및 회원탈퇴, 정보 변경 이벤트 가능 한 버튼으로 구성
	정보수정 : [담당자 이시형] 기존 정보를 Update할 수 있는 화면으로 입력이 가능케 하여(EditText) 사용자 정보를 Update할 정보를 입력하는 화면
	회원탈퇴 : [담당자 이시형] 사용자 정보를 삭제하기 위해 거치는 화면으로 아이디와 패스워드를 입력(EditText)후 버튼을 누르면 데이터 삭제
	시간표 : [담당자 이성민] TableLayout을 사용하여 6X7의 표를 만들어 시간표 틀을 생성하고 내용을 추가할 수 있는 버튼으로 구성

5. 화면 내 기초 기능 추가 구현
	시간표 : [담당자 이성민] 시간표 내용 추가 버튼 클릭 시 다이얼로그를 띄워 입력 가능케 구현
	급식표 : [담당자 이시형] GridView를 사용하여 Caledar를 생성해 날짜별 급식을 추가 및 확인이 가능하도록 구상. GridView 하단에 금일 날짜에 맞는 급식을 확인할 수 있는 TextView와 급식을 추가할 내용을 입력할 EditText로 구성

6. 어플 구동 중 필요한 데이터를 저장 할 데이터베이스를 Android Studio와 연동
	닷홈 : 데이터베이스 테이블을 생성할 수 있는 사이트로 어플 구동시 저장 할 데이터를 데이터베이스화 함. User 테이블 생성
	파일질라 : 데이터베이스를 구현한 URL을 포트에 연결하여 프로그램과 연동이 가능하도록 하는 프로그램. SQL문(.php)을 작성해 동작
	JAVA Class : URL과 HashMap으로 파일질라에 php에 필요한 정보를 넘겨주는 Class
	회원가입 : 가입 시 입력한 정보를 파일질라를 이용해 닷홈에 User 테이블에 데이터 저장
	Home : [담당자 최강현] 금일 스케쥴과 시간표를 확인 할 ListView와 광고 및 공지사항을 확인 할 TextView, 금일 급식을 보여주는 TextView로 구성
	급식표 : [담당자 이시형] Calendar 틀을 생성한 GridView에 내용을 입력해 Calendar 완성

7. 다른 화면 추가 구현 및 기능 보완
	시간표 : [담당자 이성민] 시간표 상단에 요일과 수강 시간을 선택 할 Spiner 배치, 추가 버튼 클릭 시 다이얼로그를 띄워 EditText로 과목 내용 입력 받아 Spiner 값을 이용해 시간표 내 위치에 맞게 내용 삽입
	하단 바 : [담당자 최강현] 희망 화면으로 이동할 있는 버튼으로 하단 바에 기존 버튼을 사용하지 않고 Fram 버튼을 이용해 Fragment로 화면 전환하도록 구현
	게시판 : [담당자 최강현]각종 게시판으로 이동할 수 있는 버튼들로 구성하여 희망 게시판의 버튼을 클릭 시 Intent로 해당 게시판으로 이동하도록 하였고, 게시글의 Tittle을 보여주는 ListView와 게시글 작성하는 버튼으로 구성하여 추가 버튼을 클릭 시 다이얼로그를 띄워 게시글 작성하도록 구현

8. 추가했던 기능 검토 및 보완
	-시간표 : [담당자 이성민] 외부 데이터베이스(닷홈, 파일질라)에 데이터 삽입, 수정, 삭제가 가능하도록 구현. 기존 시간표 상단에 위치한 Spiner를 디자인을 위해 xml파일을 생성해 EditText와 Spiner, 확인, 취소 버튼을 한 Page로 만들고 다이얼로그에 xml을 띄워 내용과 요일/시간을 한번에 설정하도록 구현
	-급식표 : [담당자 이시형] 기존 GridView 하단에 위치한 급식 추가 EditText를 GridView로 생성한 Calendar내 날짜 클릭 이벤트로 다이얼로그를 띄워 다이얼로그로 EditText를 이용해 내용을 받을 수 있도록 기능 보완

9. 회의를 통한 추가 화면 구현 및 데이터베이스 실제 구현(데이터 삽입, 수정, 삭제)
	-게시판 : [담당자 최강현] 기존 게시글 추가 기능은 Tittle없이 내용만 입력 했었으나 다이얼로그에 2개의 EditText를 이용해 Tittle과 내용 2가지를 받고 게시판 화면 ListView에는 Tittle만 보여주고 Tittle을 클릭 시 해당 게시글의 내용과 댓글들을 확인할 수 있는 화면을 Intent로 전환해 주는 기능 구현. 게시글 추천 버튼도 구성 하여 추후 핫게시판도 활성화할 수 있도록 구현. 게시글 삭제(DB 삭제) 및 댓글 입력(DB 삽입) 기능 구현
	-급식표 : [담당자 이시형] 급식 내용을 데이터베이스에 연동하였고 날짜 클릭 시 다이얼로그를 띄우는데 데이터베이스에 해당 날짜에 맞는 급식(데이터)이 없는 경우 급식이 없음을 보여주고 추가 등의 이벤트가 가능하고 급식이 있는 경우 데이터베이스에서 날짜에 맞는 급식(데이터)를 가져와 TexView로 보여주고 수정 등의 이벤트가 가능하도록 구현, 또한 교사일 경우만 급식표를 수정, 삭제하도록 기능을 구현
	-My Page : [담당자 이시형] 데이터베이스와 연동하여 회원탈퇴 버튼 클릭 시 정보를 입력하면 일치하는 정보를 데이터베이스에서 Delete하고 정보수정 버튼 클릭 시 입력한 정보를 Update하는 기능 구현

10. 회의를 통해 각 화면 내 기능 검토 및 추가 보완, 팀원 간 분담 화면 한 프로젝트로 구동 시작
	-시간표 : [담당자 이성민] 기존 데이터베이스에 연동은 하였으나 Delete기능을 구현하지 않아 Del버튼을 추가해 버튼 클릭 시 다이얼로그에 Spiner를 띄워 위치 값만 받아 해당 값에 있는 데이터 Delete 기능 추가히였고 기능 보완은 추가 버튼 클릭 시 데이터가 없는 경우 Insert하고 데이터가 있는 경우 Update하도록 구현
	-코드 합치기 : 하나의 어플로 구동하기 위해 하나의 프로젝트에 각자 구현한 페이지를 합치기 시작(로그인, 홈, 급식표, My Page)

11. 어플 내 화면 디자인 수정 및 팀원 간 분담한 화면 한 프로젝트로 구동
	-로딩화면 	: [담당자 최강현] 어플 구동 시 로그인 이전 어플을 상징할 Image와 어플 이름을 띄워 로딩 화면 구현
	-디자인 수정 : 전제척으로 어플의 색감 지정
	-코드 합치기 : 기존 합친 코드에 시간표 화면까지 코드 합침

12. 어플 내 화면 디자인 수정 및 자동 로그인 기능 및 어플 종료 이벤트 기능 추가 구현
	-디자인 수정 : 전체적 색감 재지정 및 기존 직사각형의 상자를 둥근 타원형으로 바꾸고 버튼들도 색감을 지정
	-자동 로그인 : 로그인 시 체크박스를 이용해 어플 재가동 시 이전 로그인 기록을 이용해 자동 로그인 기능을 추가 구현
	-홈 화면 : [담당자 최강현] 금일 날짜에 맞는 시간표를 데이터베이스에서 가져와 ListView에 띄워 확인 가능케 함

13. 최종 디자인 수정 및 회의를 통해 추가할 화면 구현
	-디자인 수정
		1)시간표 : 시간표 화면 내 버튼에 색감을 지정하고 시간표도 어플의 색감과 비슷한 색감을 지정하고 기존 화면에 비해 크기가 작은 시간표를 키워 화면 내 여백을 최대한 없애 디자인 수정
		2)홈 화면 : 금일 스케쥴와 시간표를 나란히 배치하여 여백을 최대한 활용하고, 실선을 넣어 나누어져 보이는 효과를 줌
	-스케쥴 화면 : [담당자 이성민] 기존 급식표 화면을 참고해 GridView로 Calendar를 생성하고 GridView하단에 TextView를 넣어 금일 일정을 보여주고 날짜 클릭 이벤트로 데이터베이스에 일정 삽입, 수정, 삭제 기능을 구현

페이지 별 연동 Class
로딩
	1) activity_loading.xml
	  -어플 로고인 ImageView android:src="@drawable/icon"
	2) Loading Class
	  -final Animation animTransRotate = AnimationUtils.loadAnimation(this,R.anim.rotate);이용하여 로딩화면 애니메이션 첨가
	  -SharedPreferences를 이용해 값이 있는 경우 Handler를 이용해 값을 넣어 자동 로그인 되어 MainFram Class로 전환
	  -값이 없는 경우 로그인 되지 않았으므로 Main_login Class로 전환해 로그인하도록 함
	  -LoginRequest JAVA Class를 이용해 파일질라 Login.php와 연동

로그인
	1) activity_main_login.xml
	  -TextBax 디자인 android:background="@drawable/text_shape"
	  -버튼 색 지정 Login : android:background="@drawable/button_shape"
	  -찾기 버튼 색 지정 : android:background="@drawable/button_shape2" / 아이콘 : app:srcCompat="@android:drawable/ic_menu_search"
	  -버튼 색 지정 TO JOIN : android:background="@drawable/button_shape1"
	2) Main_login
	  -use_user JAVA Class
	  -LoginRequest JAVA Class 구현 및 파일질라 Login.php와 연동
	  -TO JOIN 버튼 클릭 시 Join_login Class로 전환
	  -돋보기 버튼 클릭 시 Find_IDPW_login Class로 전환

회원가입
	activity_join_login.xml
	-회원가입 Tittle 색 지정 android:background="#596889"
	-버튼 색 지정 android:background="@drawable/button_shape4"
	Join_login
	-ValidateRequest JAVA Class 구현 및 파일질라 UserValidate.php와 연동하여 아이디 중복값 확인
	-회원가입 후 RegisterRequest JAVA Class 구현 및 파일질라 RegisterRequest.php와 연동하여 입력값 삽입

아이디 / 패스워드 찾기
	activity_find__i_d_p_w_loging.xml
	-버튼 디자인 android:background="@drawable/button_shape4"
	Find_IDPW_login
	-아이디 찾기 시 FIndID_Request JAVA Class 구현 및 파일질라 FindID.php와 연동
	-패스워드 찾기 시 FindIDPW_Request JAVA Class 구현 및 파일질라 FindPW.php와 연동
	-패스워드 찾고 정보가 있는 경우 SetPW_login Class로 화면 전환
		ºactivity_set_p_w_login.xml
		-버튼 지정 android:background="@drawable/button_shape4"
		ºSetPW_login Class
		-Update_User_PW JAVA Class 구현 및 Update_User_PW.php와 연동

홈
	activity_home.xml
	-할 일 버튼 디자인 android:background="@drawable/button_shape1"
	-시간표 버튼 디자인 android:background="@drawable/button_shape2"
	Activity_Home Class
	-할 일 버튼 클릭 시 ScheduleActivity Class로 전환
	-시간표 버튼 클릭 시 AddTimeTable Class로 전환
	-getData("http://highschool.dothome.co.kr/HomegetTimeTable.php", result);를 이용해 닷홈에 시간표 테이블에서 금일 시간표 가져옴
	-Cafeteria_Request JAVA Class를 이용해 저장되어 있는 급식을 불러옴

시간표
	activity_add_time_table.xml
	-시간표 색 지정 background="@drawable/table_inside_schema"
	-Del 버튼 색 지정 android:background="@drawable/button_shape2"
	-Ins 버튼 색 지정 android:background="@drawable/button_shape1"
	AddTImeTableActivity Class
	-버튼 클릭 시 생성 될 Dialog 화면 dialog_add.xml 및 dialog_delete.xml에 요일 Spiner 값 android:entries="@array/day" / 수강 시간 android:entries="@array/time"
	-시간표 테이블에서 데이터를 가져와 화면에 띄우는 메소드 getData("http://highschool.dothome.co.kr/getTimeTable.php");
	-데이터 베이스 호출 TImeTable_Request JAVA Class 구현 및 파일질라 TimeTable_Request.php와 연동
	-삽입 Register_Time JAVA Class 구현 및 파일질라 Register_Time.php와 연동
	-수정 Update_Time JAVA Class 구현 및 파일질라 Update_Time.php와 연동
	-삭제 Delete_Time JAVA Class  구현 및 파일질라 Delete_Time.php와 연동


급식표
	activity_cafeteria.xml
	-지난달 버튼 디자인 background="@drawable/ic_keyboard_arrow_left_black_24dp"
	-다음달 버튼 디자인 background="@drawable/ic_keyboard_arrow_right_black_24dp"
	-gridview의 배경 디자인 background="@drawable/gv_cafe_day", gridview_selector.xml을 통해 기본 패딩 삭제
	달력의 각 날짜의 layout은 cafeteria_day를 사용하였다.
	Cafeteria.java
	-gridview 구현을 위해 adapter폴더의 cafeteria_Adapter class사용, domain폴더의 DayInfo class사용
	-(DB와 연동 ) 호출 : Cafeteria_Request.java로 Cafeteria.php와 연동.
	-(DB와 연동 ) 추가 : Register_Cafe.java로 Register_Cafe.php와 연동.
	-(DB와 연동 ) 수정 : Update_Cafe.java로 Update_Cafe.php와 연동.
	-교사가 급식 달력을 클릭 할 시 다이얼로그 창이 뜨도록 구현.  dialog창의 layout은 cafeteria_dialog.xml 사용.
	cafeteria_dialog.xml
	-취소 버튼 디자인 background="@drawable/button_shape2"
	-추가 버튼 디자인 background="@drawable/button_shape1"

	

게시판
	activity_noticeboard.xml
	- 게시판 버튼 디자인 android:background="@drawable/button_shape3"
	-기타 하단 버튼 디자인 android:background="@drawable/button_shape4"
	Noticeboard Class
	-버튼 클릭시 Intent로 화면 전환
		ºactivity_notice_board.xml
		-버튼 디자인 android:background="@drawable/button_shape1"
		ºNotice_Borard Class
		-getData("http://wkwjsrjekffk.dothome.co.kr/BoardSet1.php");를 이용해 DB연동
		-ListView Item 클릭 시 Clickboard Class로 전환
		-RegisterRequest_Board JAVA Class 구현 및 Regisger_Board.php와 연동
		ºactivity_free_board.xml
		-버튼 디자인 android:background="@drawable/button_shape1"
		ºFree_Borard Class
		-getData("http://wkwjsrjekffk.dothome.co.kr/BoardSet1.php");를 이용해 DB연동
		-ListView Item 클릭 시 Clickboard Class로 전환
		-RegisterRequest_Board JAVA Class 구현 및 Regisger_Board.php와 연동
		ºactivity_hot_board.xml
		-버튼 디자인 android:background="@drawable/button_shape1"
		ºHot_Borard Class
		-getData("http://wkwjsrjekffk.dothome.co.kr/HotBoardSet.php");를 이용해 DB연동
		-ListView Item 클릭 시 Clickboard Class로 전환
		-RegisterRequest_Board JAVA Class 구현 및 Regisger_Board.php와 연동
		ºactivity_clickboard.xml
		-ImageView : app:srcCompat="@drawable/ic_person_black_24dp"
		-버튼 디자인 삭제 : android:background="@drawable/button_shape" / 수정 : android:background="@drawable/button_shape1" / 추천 : android:background="@drawable/button_shape2" / 확인 : android:background="@drawable/button_shape4"
		ºClickboard Class
		-DBgetcomments("http://wkwjsrjekffk.dothome.co.kr/Comments_Get.php");를 이용해 DB연동해 값 불러옴
		-추천 수 : hotcheckRequest JAVA Class 구현 및 horcheck.php와 연동
		-게시글 삭제 : removeRequest JAVA Class 구현 및 Board_remove.php와 연동
		-게시글 수정 : UpdateRequest_Board JAVA Class 구현 및 Update_Board.php와 연동
		-댓글 추가 : CommentesRequest_Board JAVA Class 구현 및 Comments_Register.php와 연동
		-댓글 수정 : UpateRequest_Comment JAVA Class 구현 및 Update_Comments.php와 연동
		-댓글 삭제 : DeleteRequest_Comment JAVA Class 구현 및 Delete_Comments.php와 연동
		-게시글 삭제 시 댓글 삭제 : DeleteRequest_WithComment JAVA Class 구현 및 Delete_WithComments.php와 연동
		-추천 수 Update : UpdateRequest_CommentCont JAVA Class 구현 및 Update_CommentsCount.phh와 연동

My Page
	activity_mypage.xml
	-로그아웃 버튼 디자인 background="@drawable/button_shape"
	-회원탈퇴 버튼 디자인 background="@drawable/button_shape1"
	-정보변경 버튼 디자인 background="@drawable/button_shape2"
	Mypage.java
	외부 DB에서 쓴글을 listview로 불러온다.
	-(DB와 연동 ) 호출 : Mypage_BoardSet.php와 연동.
	listview를 클릭 할 경우 Clickboard.java로 이동. Clickboard.java는 게시판에서 설명한다.
	외부 DB에서 쓴 댓글을 listview로 불러온다.
	-(DB와 연동 ) 호출 : Mypage_CommentSet.php와 연동.
	listview를 클릭 할 경우 listview를 클릭 할 경우 Clickboard.java로 이동. Clickboard.java는 게시판에서 설명한다.
	-로그아웃 버튼 클릭 시 Main_login.java로 이동. Main_login.java는 로그인 페이지에서 설명한다.
	-회원탈퇴 버튼 클릭 시 withdraw.java로 이동. layout 파일은 activity_withdraw.xml 사용.
	-정보변경 버튼 클릭 시 ChangeInformation.java로 이동. layout 파일은 activity_change_information.xml 사용.
	withdraw.java
	-(DB와 연동) 삭제 : Delet_User.java로 Delet_User.php와 연동
	activity_withdraw.xml
	-회원탈퇴 버튼 디자인 background="@drawable/button_shape4
	ChangeInformation.java
	-(DB와 연동) 수정 : Update_User.java로 Update_User.php와 연동
	activity_change_information.xml
	-확인 버튼 디자인 background="@drawable/button_shape1

	
스케쥴
	activity_schedule.xml
	-지난달 버튼 디자인 background="@drawable/ic_keyboard_arrow_left_black_24dp"
	-다음달 버튼 디자인 background="@drawable/ic_keyboard_arrow_right_black_24dp"
	-일정 추가 버튼 디자인 background="@drawable/button_shape1"
	-gridview의 배경 디자인 background="@drawable/gv_cafe_day", gridview_selector.xml을 통해 기본 패딩 삭제
	-달력의 각 날짜의 layout은 cafeteria_day.xml을 사용하였다.
	ScheduleActivity.java
	-gridview 구현을 위해 adapter폴더의 cafeteria_Adapter class사용, domain폴더의 DayInfo class사용
	-listview 구현을 위해 ScheduleAdapter class에서 Scheduleitem.java, ScheduleitemView사용
	-ScheduleitemView의 layout 파일로 schedule_list_item.xml 사용.
	-(DB와 연동 ) 호출 : ScheduleTable.php와 연동.
	-(DB와 연동 ) 확인 :  Schedule.java로  Schedule.php와 연동.
	-(DB와 연동 ) 추가 : Register_Schedule.java로 Register_Schedule.php와 연동.
	-(DB와 연동 ) 수정 : Update_Schedule.java로 Update_Schedule.php와 연동.
	-(DB와 연동 ) 삭제 : Delete_Schedule.java로 Delete_Schedule.php와 연동.
	-일정 추가버튼 클릭시 dialog창 구현. dialog창의 layout은 schedule_dialog.xml 사용.
	-listview 클릭시 dialog창 구현. dialog 창의 수정 버튼 클릭 시 dialog창 구현. dialog창의 layout은 schedule_dialog_update.xml 사용.
	schedule_dialog.xml 
	-취소 버튼 디자인 background="@drawable/button_shape2"
	-추가 버튼 디자인 background="@drawable/button_shape1"
	schedule_dialog_update.xml 
	-취소 버튼 디자인 background="@drawable/button_shape2"
	-수정 버튼 디자인 background="@drawable/button_shape1"


폰트
	-커스텀 폰트를 사용하기 위해 res폴더에 font폴더를 만든다.
	-font폴더 안에 폰트 커스텀 폰트(nanumsquareroundb.ttf)를 넣고 font_sytle.xml와 연결한다.
	-values폴더의 sytles.xml에서 폰트를 사용하고 싶은 item에 폰트를 사용한다. 사용법은 styles.xml의 주석을 참고.


외부 DB
	-외부 DB로 MyDotHome을 사용하였다.
	-FileZila를 사용하여 DB에 접속해 php 파일을 만들어 넣었다.
	-안드로이드 스튜디오에서 php파일을 사용해 DB와 연결하였다.
	-안드로이드 스튜디오에서 php파일에 접속 하기 위해 Gradle Scripts의 build.gradle(Module: app)파일에서  dependencies 에 implementation 'com.android.volley:volley:1.1.1'를 추가한다.
	-안드로이드 스튜디오에서 php파일에 접속 하기 위해 manifests폴더의 AndroidManifest.xml에 <uses-permission android:name="android.permission.INTERNET" />을 추가한다.
	-안드로이드 스튜디오에서 php파일에 접속 하기 위해 manifests폴더의 AndroidManifest.xml의 <application>에 android:usesCleartextTraffic="true"를 추가한다.


수익화 방안
	-홈화면의 공지사항으로 광고를 넣는다.
	-어플 하단에 광고 배너를 넣는다.
	-게시판 화면의 공모전 탭에 공모전을 홍보하여 홍보 비용으로 수익을 창출한다.

실용성
	-실제 학교들과 제휴하여 사용할 수 있음.
	-외부 DB의 크기가 더 커진다면 많은 유저들의 사용이 가능하다.
