package beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Expense {
	/**
	 * データベースの文字数制限
	 */
	public static final int MAX_EXPID_LENGTH = 10;
	public static final int MAX_APPLICANT_LENGTH = 20;
	public static final int MAX_TITLE_LENGTH = 100;
	public static final int MAX_PAYEE_LENGTH = 20;
	public static final int MAX_CHANGER_LENGTH = 10;

	/**
	 * 保持データ
	 */
	private int id;
	private String expenseId;
	private String applyDate;
	private String changeDate;
	private String applicant;
	private String title;
	private String payee;
	private int price;
	private Status status;
	private String changer;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(String expenseId) {
		this.expenseId = expenseId;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getChanger() {
		return changer;
	}

	public void setChanger(String changer) {
		this.changer = changer;
	}

	/**
	 * オブジェクトのデータが有効かどうか調べます。
	 *
	 * @return 有効な場合は true を返す
	 */
	public boolean isValidObject() {
		if ((expenseId == null) || (expenseId.getBytes().length > MAX_EXPID_LENGTH)) {
			System.err.println("Expense: Bad expenseid length.");
			return false;
		}
		if ((applicant == null) || (applicant.getBytes().length > MAX_APPLICANT_LENGTH)) {
			System.err.println("Employee: Bad applicant length.");
			return false;
		}
		if ((title == null) || (title.getBytes().length > MAX_TITLE_LENGTH)) {
			System.err.println("Employee: Bad title length.");
			return false;
		}
		if ((payee == null) || (payee.getBytes().length > MAX_PAYEE_LENGTH)) {
			System.err.println("Employee: Bad payee length.");
			return false;
		}
		if ((changer == null) || (changer.getBytes().length > MAX_CHANGER_LENGTH)) {
			System.err.println("Employee: Bad changer length.");
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getId());
		sb.append(",");
		sb.append(this.getExpenseId());
		sb.append(",");
		sb.append(this.getApplyDate());
		sb.append(",");
		sb.append(this.getChangeDate());
		sb.append(",");
		sb.append(this.getApplicant());
		sb.append(",");
		sb.append(this.getTitle());
		sb.append(",");
		sb.append(this.getPayee());
		sb.append(",");
		sb.append(this.getPrice());
		sb.append(",");
		sb.append(this.getStatus());
		sb.append(",");
		sb.append(this.getChanger());
		sb.append(",");


		String s = this.getZip();
		sb.append(s != null ? s : "");
		sb.append(",");
		s = this.getPref();
		sb.append(s != null ? s : "");
		sb.append(",");
		s = this.getAddress();
		sb.append(s != null ? s : "");
		sb.append(",");
		sb.append(this.getPost().getId());
		sb.append(",");
		String date = this.getEnterDate();
		sb.append(date != null ? date : "");
		sb.append(",");
		date = this.getRetireDate();
		sb.append(date != null ? date : "");
		// バッファに書き出します
		return sb.toString();
	}

}
