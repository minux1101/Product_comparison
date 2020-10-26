import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ListDAO {
	public static final String LIST_TABLE_NAME = "list"; // MySQL의 list 테이블을 상수로 선언해서 받아옴
	public static final String LIST_COLUMN_NO = "no"; // 리스트 테이블의 컬럼 no
	public static final String LIST_COLUMN_ITEM = "item"; // 리스트 테이블의 컬럼 item
	public static final String LIST_COLUMN_WEIGHT = "weight"; // 리스트 테이블의 컬럼 weight
	public static final String LIST_COLUMN_DISPLAY = "display"; // 리스트 테이블의 컬럼 display
	public static final String LIST_COLUMN_GB = "gb"; // 리스트 테이블의 컬럼 gb
	public static final String LIST_COLUMN_etc = "etc"; // 리스트 테이블의 컬럼 etc
	public static final String LIST_COLUMN_PRICE = "price"; // 리스트 테이블의 컬럼 price

	public static Connection connect() {
		Connection con = null;

		String server = "localhost"; // MySQL 서버 주소 127.0.0.1
		String database = "test"; // MySQL DATABASE 이름
		String user_name = "root"; // MySQL 서버 아이디
		String password = ""; // MySQL 서버 비밀번호

		// 2.연결
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + "?allowPublicKeyRetrieval=true&characterEncoding" + "=UTF-8&serverTimezone=UTC&useSSL=false", user_name,
					password);
		} catch (SQLException e) {
			System.err.println("con 오류:" + e.getMessage()); // MySQL과 연결되지 않으면 에러가 왜 났는지 출력된다.
		}

		return con;
	}

	public static void connectionClose(Connection con) {
		// 3.해제
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			System.err.println("con 오류:" + e.getMessage()); // MySQL과 연결되지 않으면 에러가 왜 났는지 출력된다.
		}
	}

	public static void select() {

		try {
			Connection con = ListDAO.connect();
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM " + LIST_TABLE_NAME);

			ResultSet resultSet = pstmt.executeQuery();

			System.out.printf("%s\t %-10s\t   %10s\t     %s\t        %s\t\t %s\t\t %s\n", "No", "Item", "무게(g)", "화면(인치)", "용량(GB)", "비고", "가격(만원)");
			while (resultSet.next()) {
				System.out.printf("%d\t", resultSet.getInt(1));
				System.out.printf("%-10s\t", resultSet.getString(2));
				System.out.printf("%10d\t\t", resultSet.getInt(3));
				System.out.printf("%d\t\t", resultSet.getInt(4));
				System.out.printf("%d\t\t ", resultSet.getInt(5));
				System.out.printf("%s\t\t ", resultSet.getString(6));
				System.out.printf("%d\n", resultSet.getInt(7));
			}
			resultSet.close();
			pstmt.close();
			con.close();
		} catch (SQLException e) {
			System.err.println("con 오류:" + e.getMessage());
		}
	}

	public static int insert() {
		int result = 0;
		Scanner scanner = new Scanner(System.in);

		boolean isIns = false;
		while (!isIns) {
			try {
				Connection con = ListDAO.connect();
				PreparedStatement pstmt = con.prepareStatement("INSERT INTO " + LIST_TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?, ?)");

				System.out.println("물품 번호를 입력하세요 : ");
				String a = scanner.next();
				System.out.println("물품 이름를 입력하세요 : ");
				String b = scanner.next();
				System.out.println("물품 무게(g)를 입력하세요 : ");
				String c = scanner.next();
				System.out.println("물품 화면크기(인치)를 입력하세요 : ");
				String d = scanner.next();
				System.out.println("물품 용량(GB)을 입력하세요 : ");
				String e1 = scanner.next();
				System.out.println("물품 비고를 입력하세요 : ");
				String f = scanner.next();
				System.out.println("물품 가격(만원)을 입력하세요 : ");
				String g = scanner.next();

				int n = Integer.parseInt(a);
				int wg = Integer.parseInt(c);
				int dis = Integer.parseInt(d);
				int ggb = Integer.parseInt(e1);
				int pr = Integer.parseInt(g);

				pstmt.setInt(1, n);
				pstmt.setString(2, b);
				pstmt.setInt(3, wg);
				pstmt.setInt(4, dis);
				pstmt.setInt(5, ggb);
				pstmt.setString(6, f);
				pstmt.setInt(7, pr);

				result = pstmt.executeUpdate(); // 몇 개가 업데이트 되었는지 반영함.

				// 결과 처리
				System.out.println(result + "건 입력되었습니다.");
				select();

				boolean isMore = false;
				while (!isMore) {
					System.out.println("더 입력하시겠습니까?");
					System.out.println("[1] 예");
					System.out.println("[2] 아니요");
					String ans = scanner.next();
					switch (ans) {
					case "1":
						insert();
						break;
					case "2":
						System.out.println("관리자 메뉴로 돌아갑니다.");
						admin();
						break;
					default:
						System.out.println("잘못 입력하셨습니다.");
						continue;
					}
					isMore = true;
					break;
				}
				pstmt.close();
				con.close();

			} catch (SQLException e) {
				System.out.println("다시 입력해 주세요.");
				continue;
			} catch (NumberFormatException e) {
				System.out.println("다시 입력해 주세요.");
				continue;
			} catch (NullPointerException e) {
				System.out.println("다시 입력해 주세요.");
				continue;
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("다시 입력해 주세요.");
				continue;
			}
			isIns = true;
			break;
		}
		return result; // 처리 갯수를 리턴
	}

	public static int delete() {
		int result = 0;
		Scanner scanner = new Scanner(System.in);

		boolean isDel = false;
		while (!isDel) {
			try {
				Connection con = ListDAO.connect();
				PreparedStatement pstmt = con.prepareStatement("DELETE FROM " + LIST_TABLE_NAME + " WHERE " + LIST_COLUMN_ITEM + " = ?;");

				System.out.println("삭제할 물품을 입력하세요. : ");
				String del = scanner.next();
				pstmt.setString(1, del);
				result = pstmt.executeUpdate();

				// 결과 처리
				System.out.println(result + "건 삭제 되었습니다.");
				select();

				boolean isMore = false;
				while (!isMore) {
					System.out.println("더 삭제하시겠습니까?");
					System.out.println("[1] 예");
					System.out.println("[2] 아니요");
					String ans = scanner.next();
					switch (ans) {
					case "1":
						delete();
						break;
					case "2":
						System.out.println("관리자 메뉴로 돌아갑니다.");
						admin();
						break;
					default:
						System.out.println("잘못 입력하셨습니다.");
						continue;
					}
					isMore = true;
					break;
				}

				pstmt.close();
				con.close();
			} catch (SQLException e) {
				System.out.println("다시 입력해 주세요.");
				continue;
			} catch (NumberFormatException e) {
				System.out.println("다시 입력해 주세요.");
				continue;
			} catch (NullPointerException e) {
				System.out.println("다시 입력해 주세요.");
				continue;
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("다시 입력해 주세요.");
				continue;
			}
			isDel = true;
			break;
		}
		return result; // 처리 갯수를 리턴
	}

	public static int update() {
		int result = 0;
		Scanner scanner = new Scanner(System.in);

		boolean isUp = false;
		while (!isUp) {
			try {
				Connection con = ListDAO.connect();
				PreparedStatement pstmt = con.prepareStatement("UPDATE " + LIST_TABLE_NAME + " SET " + LIST_COLUMN_PRICE + " = ? WHERE " + LIST_COLUMN_ITEM + " = ?;");

				System.out.println("수정할 물품을 입력하세요. : ");
				String a = scanner.next();
				System.out.println("물품 가격(만원)을 입력하세요 : ");
				String b = scanner.next();

				int p = Integer.parseInt(b);

				pstmt.setInt(1, p);
				pstmt.setString(2, a);

				result = pstmt.executeUpdate(); // 몇 개가 업데이트 되었는지 반영함.
				System.out.println(result + "건 수정되었습니다.");
				select();

				boolean isMore = false;
				while (!isMore) {
					System.out.println("더 수정하시겠습니까?");
					System.out.println("[1] 예");
					System.out.println("[2] 아니요");
					String ans = scanner.next();
					switch (ans) {
					case "1":
						update();
						break;
					case "2":
						System.out.println("관리자 메뉴로 돌아갑니다.");
						admin();
						break;
					default:
						System.out.println("잘못 입력하셨습니다.");
						continue;
					}
					isMore = true;
					break;
				}

				pstmt.close();
				con.close();

			} catch (SQLException e) {
				System.out.println("다시 입력하세요.");
				continue;
			} catch (NumberFormatException e) {
				System.out.println("다시 입력하세요.");
				continue;
			} catch (NullPointerException e) {
				System.out.println("다시 입력하세요.");
				continue;
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("다시 입력해 주세요.");
				continue;
			}
			isUp = true;
			break;
		}
		return result;
	}

	public static void start() { // 초기 화면 메서드
		String menu = ""; // 선택한 메뉴를 입력 받을 변수를 선언
		Scanner scanner = new Scanner(System.in); // 스캐너 생성

		boolean isStart = false; // while문을 빠져나올 수 있는 장치로 boolean 변수 isStart를 선언하고, false로 초기화 한다.
		while (!isStart) { // !isLog(isLog가 아니다)는 true가 되므로 while문이 실행된다. while문은 true일 때만 반복해서 실행된다.
			System.out.println("######### 메뉴 ##########");
			System.out.println("[1] 물품 비교");
			System.out.println("[2] 관리자 모드");
			System.out.println("[0] 초기화면");
			System.out.print("메뉴를 선택해 주세요. : ");

			menu = scanner.next(); // 선택한 메뉴를 스캐너로 입력받는다.
			switch (menu) {
			case "1": // 선택한 메뉴가 1일 때
				compare(); // compare 메서드를 호출해서 물품 비교를 한다.
				break; // break로 switch 탈출
			case "2": // 선택한 메뉴가 2일 때
				logIn(); // 관리자 모드를 실행하기 위해서는 관리자 로그인을 해야한다. 관리자 로그인 메서드 logIn을 호출한다.
				break; // break로 switch 탈출
			case "0": // 선택한 메뉴가 0일 때
				start(); // 초기화면으로 가기 위해서 start 메서드를 호출한다.
				break; // break로 switch 탈출
			default: // 입력받은 값이 1, 2, 0 이 아닐경우
				System.out.println("잘못 입력하셨습니다.");
				continue; // while문의 처음으로 돌아간다.
			}
			isStart = true; // isStart 값을 true로 변경해주면 while문의 조건은 false가 되어 실행되지 않는다.
			break; // while문을 탈출한다.
		}
	}

	public static void compare() { // 물품 비교 메서드
		String back = ""; // 계속할지 말지를 입력 받을 변수를 선언
		String itemA = ""; // 비교할 아이템A의 번호를 입력 받을 변수를 선언
		String itemB = ""; // 비교할 아이템B의 번호를 입력 받을 변수를 선언
		int sumA; // 선택한 아이템A의 총점
		int sumB; // 선택한 아이템A의 총점
		int scoreWA = 1; // 아이템 A 조건1(무게)의 점수를 선언, 나중에 값을 곱해줄 거라서 1로 초기화
		int scoreDA = 1; // 아이템 A 조건2(화면크기)의 점수를 선언, 나중에 값을 곱해줄 거라서 1로 초기화
		int scoreGBA = 1; // 아이템 A 조건3(용량)의 점수를 선언, 나중에 값을 곱해줄 거라서 1로 초기화
		int scorePA = 1; // 아이템 A 조건4(가격)의 점수를 선언, 나중에 값을 곱해줄 거라서 1로 초기화
		int scoreWB = 1; // 아이템 B 조건1(무게)의 점수를 선언, 나중에 값을 곱해줄 거라서 1로 초기화
		int scoreDB = 1; // 아이템 B 조건2(화면크기)의 점수를 선언, 나중에 값을 곱해줄 거라서 1로 초기화
		int scoreGBB = 1; // 아이템 B 조건2(화면크기)의 점수를 선언, 나중에 값을 곱해줄 거라서 1로 초기화
		int scorePB = 1; // 아이템 B 조건2(화면크기)의 점수를 선언, 나중에 값을 곱해줄 거라서 1로 초기화
		int itmA; // itemA를 int형으로 바꾸기 위해 itmA 선언
		int itmB; // itemB를 int형으로 바꾸기 위해 itmB 선언
		int result;
		Scanner scanner = new Scanner(System.in); // 스캐너 생성

		System.out.println("###### 물품 리스트 #######");
		select(); // select 메서드를 호출해서 데이터베이스에 있는 물품리스트를 보여준다.

		boolean isBack = false; // while문을 빠져나올 수 있는 장치로 boolean 변수 isLog을 선언하고, false로 초기화 한다.
		while (!isBack) { // !isBack(isBack이 아니다)는 true가 되므로 while문이 실행된다. while문은 true일 때만 반복해서 실행된다.
			System.out.println("계속 비교 하시려면 1, 상위 메뉴로 가시려먼 -1을 입력하세요.");
			System.out.print("메뉴를 선택해주세요. : ");
			back = scanner.next(); // 비교를 계속할지 상위 메뉴로 갈지 대답을 입력받는다.
			switch (back) { // switch 문을 사용해서 입력받은 back의 값에 따라서 결과를 도출한다.
			case "-1": // back가 -1 일 때
				System.out.println("상위 메뉴로 이동합니다.");
				start(); // 상위 메뉴로 이동하기 위해서 start 메서드를 호출한다.
				break; // break로 switch 탈출
			case "1": // back가 1일 때
				break; // 계속 비교를 위해서 break로 switch 탈출
			default: // 1과 -1 외의 다른 값을 입력받았을 때
				System.out.println("잘못 입력하셨습니다.");
				continue; // while문의 처음으로 다시 돌아간다.
			}
			isBack = true; // isBack 값을 true로 변경해주면 while문의 조건은 false가 되어 실행되지 않는다.
			break; // while문을 탈출한다.
		}

		boolean isCompare = false; // while문을 빠져나올 수 있는 장치로 boolean 변수 isCompare을 선언하고, false로 초기화 한다.
		while (!isCompare) { // !isCompare(isCompare가 아니다)는 true가 되므로 while문이 실행된다. while문은 true일 때만 반복해서
								// 실행된다.
			try {
				Connection con = ListDAO.connect(); // 데이터베이스와 연결하는 메서드를 호출
				PreparedStatement pstmt1 = con
						.prepareStatement("CREATE OR REPLACE VIEW list_view AS SELECT * FROM " + LIST_TABLE_NAME + " WHERE " + LIST_COLUMN_NO + " = ? OR " + LIST_COLUMN_NO + " = ?");
				System.out.print("비교할 아이템의 번호를 선택해 주세요. :");
				itemA = scanner.next();
				System.out.print("비교할 아이템의 번호를 선택해 주세요. :");
				itemB = scanner.next();

				itmA = Integer.parseInt(itemA);
				itmB = Integer.parseInt(itemB);
				pstmt1.setInt(1, itmA);
				pstmt1.setInt(2, itmB);
				result = pstmt1.executeUpdate();

				System.out.println("###### 물품 비교 결과 ########");
				System.out.printf("%s\t %-10s\t   %10s\t     %s\t        %s\t\t %s\t\t %s\n", "No", "Item", "무게(g)", "화면(인치)", "용량(GB)", "비고", "가격(만원)");

				PreparedStatement pstmt2 = con.prepareStatement("SELECT * FROM list_view WHERE " + LIST_COLUMN_NO + " = ?");
				pstmt2.setInt(1, itmA);
				ResultSet resultSet1 = pstmt2.executeQuery();

				PreparedStatement pstmt3 = con.prepareStatement("SELECT * FROM list_view WHERE " + LIST_COLUMN_NO + " = ?");
				pstmt3.setInt(1, itmB);
				ResultSet resultSet2 = pstmt3.executeQuery();

				String view[][] = new String[2][7];

				while (resultSet1.next()) {
					for (int i = 0; i < 7; i++) {
						view[0][i] = resultSet1.getString(i + 1);
					}
				}

				while (resultSet2.next()) {
					for (int i = 0; i < 7; i++) {
						view[1][i] = resultSet2.getString(i + 1);
					}
				}

				for (int i = 0; i < 2; i++) {
					System.out.printf("%s\t", view[i][0]);
					System.out.printf("%-10s\t", view[i][1]);
					System.out.printf("%10s\t\t", view[i][2]);
					System.out.printf("%s\t\t", view[i][3]);
					System.out.printf("%s\t\t ", view[i][4]);
					System.out.printf("%s\t\t ", view[i][5]);
					System.out.printf("%s\n", view[i][6]);
				}

				int wA = Integer.parseInt(view[0][2]);
				int dA = Integer.parseInt(view[0][3]);
				int gbA = Integer.parseInt(view[0][4]);
				int pA = Integer.parseInt(view[0][6]);

				int wB = Integer.parseInt(view[1][2]);
				int dB = Integer.parseInt(view[1][3]);
				int gbB = Integer.parseInt(view[1][4]);
				int pB = Integer.parseInt(view[1][6]);

				if (wA < wB) {
					scoreWA *= 20;
					scoreWB *= 0;
					System.out.println("*무게 : " + itmA + " > " + itmB);
				} else if (wB < wA) {
					scoreWB *= 20;
					scoreWA *= 0;
					System.out.println("*무게 : " + itmB + " > " + itmA);
				} else {
					scoreWA *= 0;
					scoreWB *= 0;
					System.out.println("*무게 : " + itmA + " = " + itmB);
				}

				if (dA > dB) {
					scoreDA *= 20;
					scoreDB *= 0;
					System.out.println("*용량 : " + itmA + " > " + itmB);
				} else if (dB > dA) {
					scoreDB *= 20;
					scoreDA *= 0;
					System.out.println("*용량 : " + itmB + " > " + itmA);
				} else {
					scoreDA *= 0;
					scoreDB *= 0;
					System.out.println("*용량 : " + itmA + " = " + itmB);
				}

				if (gbA > gbB) {
					scoreGBA *= 20;
					scoreGBB *= 0;
					System.out.println("*용량 : " + itmA + " > " + itmB);
				} else if (gbB > gbA) {
					scoreGBB *= 20;
					scoreGBA *= 0;
					System.out.println("*용량 : " + itmB + " > " + itmA);
				} else {
					System.out.println("*용량 : " + itmA + " = " + itmB);
					scoreGBA *= 0;
					scoreGBB *= 0;
				}

				if (pA < pB) {
					scorePA *= 40;
					scorePB *= 0;
					System.out.println("*가격 : " + itmA + " > " + itmB);
				} else if (pB < pA) {
					scorePB *= 40;
					scorePA *= 0;
					System.out.println("*가격 : " + itmB + " > " + itmA);
				} else {
					scorePA *= 0;
					scorePB *= 0;
					System.out.println("*가격 : " + itmA + " = " + itmB);
				}

				sumA = scoreWA + scoreDA + scoreGBA + scorePA;
				sumB = scoreWB + scoreDB + scoreGBB + scorePB;

				System.out.println("- " + view[0][1] + " : 무게 (" + scoreWA + ") + 화면 (" + scoreDA + ") + 용량 (" + scoreGBA + ") + 가격 (" + scorePA + ") = " + sumA);
				System.out.println("- " + view[1][1] + " : 무게 (" + scoreWB + ") + 화면 (" + scoreDB + ") + 용량 (" + scoreGBB + ") + 가격 (" + scorePB + ") = " + sumB);

				if (sumA > sumB) {
					System.out.println("- 최종 추천 : " + view[0][1]);
				} else if (sumB > sumA) {
					System.out.println("- 최종 추천 : " + view[1][1]);
				} else {
					System.out.println("- 최종 추천 : " + view[0][1] + " = " + view[1][1]);
				}

				boolean isMore = false;
				while (!isMore) {
					System.out.println("더 비교하시겠습니까?");
					System.out.println("[1] 예");
					System.out.println("[2] 아니요");
					String ans = scanner.next();
					switch (ans) {
					case "1":
						compare();
						break;
					case "2":
						System.out.println("상위 메뉴로 이동합니다.");
						start();
						break;
					default:
						System.out.println("잘못 입력하셨습니다.");
						continue;
					}
					isMore = true;
					break;
				}

				resultSet1.close();
				resultSet2.close();
				pstmt1.close();
				pstmt2.close();
				pstmt3.close();
				con.close();

			} catch (SQLException e) {
				System.out.println("다시 입력하세요.");
				continue;
			} catch (NumberFormatException e) {
				System.out.println("다시 입력해 주세요.");
				continue;
			} catch (NullPointerException e) {
				System.out.println("다시 입력해 주세요.");
				continue;
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("다시 입력해 주세요.");
				continue;
			}
			isCompare = true;
			break;
		}

	}

	public static void logIn() { // 관리자 로그인 메서드
		String ad = ""; // 관리자 id를 입력 받을 변수를 선언
		String adpass = ""; // 관리자 비밀번호를 입력 받을 변수를 선언
		Scanner scanner = new Scanner(System.in); // 스캐너 생성

		boolean isLog = false; // while문을 빠져나올 수 있는 장치로 boolean 변수 isLog을 선언하고, false로 초기화 한다.
		while (!isLog) { // !isLog(isLog가 아니다)는 true가 되므로 while문이 실행된다. while문은 true일 때만 반복해서 실행된다.
			System.out.println("###### 관리자 로그인 #######");
			System.out.print("id : ");
			ad = scanner.next(); // 관리자 id를 스캐너로 입력받는다.
			System.out.print("비밀번호 : ");
			adpass = scanner.next(); // 관리자 id를 스캐너로 입력받는다.

			if (ad.equals("admin") && adpass.equals("admin")) { // id가 admin, 비밀번호가 admin이면 관리자이다.
				System.out.println("관리자 모드를 실행합니다.");
				select(); // 관리자 모드를 실행하면 일단 select 메서드를 호출해서 모든 데이터를 보여준다.
				admin(); // 관리자 메뉴인 admin 메서드를 호출한다.
				break;
			} else { // id와 비밀번호가 틀릴 경우 else문이 실행된다.
				System.out.println("로그인에 실패했습니다.");
				System.out.println("로그인을 계속 하시겠습니까?"); // 로그인을 계속 할 건지 여부를 물어본다.
				System.out.println("[1] 예");
				System.out.println("[2] 아니오");
				System.out.print("메뉴를 선택해 주세요. : ");
				String ans = scanner.next(); // ans 변수를 선언해서 대답을 스캐너로 입력 받는다.
				switch (ans) { // switch 문을 사용해서 입력받은 ans의 값에 따라서 결과를 도출한다.
				case "1": // ans가 1이면 다시 로그인을 실행한다.
					logIn(); // 로그인을 다시 하기 위해서 logIn 메서드를 호출한다.
					break; // break로 switch 탈출
				case "2":
					System.out.println("상위 메뉴로 돌아갑니다."); // ans가 2이면 로그인을 포기하므로 상위 메뉴로 돌아간다.
					start(); // 상위 메뉴는 초기메뉴 이므로 첫 화면인 start 메서드를 호출한다.
					break; // break로 switch 탈출
				default: // 입력 받은 값이 1과 2가 아니면 default가 된다.
					System.out.println("잘못 입력하셨습니다.");
					continue; // 다시 while문의 처음으로 돌아간다.
				}
			}
			isLog = true; // isLog 값을 true로 변경해주면 while문의 조건은 false가 되어 실행되지 않는다.
			break; // while문을 탈출한다.
		}
	}

	public static void admin() { // 관리자 메뉴 메서드
		String admenu = ""; // 스캐너를 통해서 입력받을 변수를 선언한다.
		Scanner scanner = new Scanner(System.in); // 스캐너 생성.

		boolean isAdmin = false; // while문을 빠져나올 수 있는 장치로 boolean 변수 isAdmin을 선언하고, false로 초기화 한다.
		while (!isAdmin) { // !isAdmin(isAdmin이 아니다)는 true가 되므로 while문이 실행된다. while문은 true일 때만 반복해서 실행된다.
			System.out.println("###### 관리자 메뉴 #######");
			System.out.println("[1] 물품 입력");
			System.out.println("[2] 물품 수정");
			System.out.println("[3] 물품 삭제");
			System.out.println("[0] 초기화면");
			System.out.println("[-1] 상위 메뉴");
			System.out.print("메뉴를 선택해주세요. : ");

			admenu = scanner.next(); // 선택할 메뉴를 스캐너로 입력.
			switch (admenu) { // switch 문을 사용해서 입력받은 admenu의 값에 따라서 결과를 도출한다.
			case "1": // 입력받은 값이 1이면 insert 메서드를 호출한다.
				insert();
				break; // break로 switch 탈출
			case "2": // 입력받은 값이 2이면 update 메서드를 호출한다.
				update();
				break; // break로 switch 탈출
			case "3": // 입력받은 값이 3이면 delete 메서드를 호출한다.
				delete();
				break; // break로 switch 탈출
			case "0": // 입력받은 값이 0이면 초기 화면인 start 메서드를 호출한다.
				System.out.println("첫 화면으로 돌아갑니다.");
				start();
				break; // break로 switch 탈출
			case "-1":
				System.out.println("상위 메뉴로 돌아갑니다.");
				logIn(); // 입력받은 값이 -1이면 상위 메뉴인 logIn 메서드를 호출해서 로그인 화면으로 돌아간다.
				break; // break로 switch 탈출
			default:
				System.out.println("잘못 입력하셨습니다."); // 입력받은 값이 1,2,3,0,-1에 해당하지 않으면 default가 된다.
				continue; // 다시 while문의 처음으로 돌아간다.
			}
			isAdmin = true; // isAdmin 값을 true로 변경해주면 while문의 조건은 false가 되어 실행되지 않는다.
			break; // while문을 탈출한다.
		}
	}

}