
public class List { // list table을 class로 생성
	private int no;
	private String item;
	private int weight;
	private int display;
	private int gb;
	private String etc;
	private int price;

	public List() { // 기본 생성자

	}

	// getter 와 setter - private로 선언했기 때문에 그대로 값을 바꾸거나 쓸 수 없어서 getter와 setter가 필요하다.
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

	public int getGb() {
		return gb;
	}

	public void setGb(int gb) {
		this.gb = gb;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override // toString으로 출력 형식을 정한다.
	public String toString() {
		return "List [no=" + no + ", item=" + item + ", weight=" + weight + ", display=" + display + ", gb=" + gb
				+ ", etc=" + etc + ", price=" + price + "]";
	}

}
