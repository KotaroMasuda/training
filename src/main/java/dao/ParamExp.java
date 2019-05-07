package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAOにクエリーパラムを引き渡すためのオブジェクト。
 */
public class ParamExp {
	private final static String BASE_WHERE_CLAUSE = " WHERE ";

	private int statusId;
	private String applicantParam;
	private String titleParam;

	public ParamExp(int statusId, String applicantParam, String titleParam) {
		this.setStatusId(statusId);
		this.setApplicantParam(applicantParam);
		this.setTitleParam(titleParam);
	}

	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getApplicantParam() {
		return applicantParam;
	}
	public void setApplicantParam(String applicantParam) {
		this.applicantParam = applicantParam == null ? "" : "%"+applicantParam+"%";
	}

	public String getTitleParam() {
		return titleParam;
	}
	public void setTitleParam(String titleParam) {
		this.titleParam = titleParam == null ? "" : "%"+titleParam+"%";
	}

	/**
	 * 登録されているパラメータの状態からWHERE句を生成する。
	 *
	 * @return SQLのWHERE句
	 */
	public String getWhereClause() {
		StringBuilder whereClause = new StringBuilder();
		if (statusId != 0) {
			if (whereClause.length() == 0) {
				whereClause.append(BASE_WHERE_CLAUSE);
			} else {
				whereClause.append(" AND ");
			}
			whereClause.append("E.STATUSID = ?");
		}
		if (!applicantParam.isEmpty()) {
			if (whereClause.length() == 0) {
				whereClause.append(BASE_WHERE_CLAUSE);
			} else {
				whereClause.append(" AND ");
			}
			whereClause.append("E.APPLICANT LIKE ?");
		}
		if (!titleParam.isEmpty()) {
			if (whereClause.length() == 0) {
				whereClause.append(BASE_WHERE_CLAUSE);
			} else {
				whereClause.append(" AND ");
			}
			whereClause.append("E.TITLE LIKE ?");
		}

		// ORDER BYは最後に指定する
		whereClause.append(" ORDER BY E.ID");

		return whereClause.toString();
	}

	/**
	 * getWhereClauseメソッドで設定されたWHERE句を含むSQLにパラメータをセットする
	 *
	 * @param statement パラメータをセットする対象のPreparedStatement
	 * @throws SQLException パラメータの設定時に何らかの問題があった場合
	 */
	public void setParameter(PreparedStatement statement) throws SQLException {
		int count = 1;
		if (statusId != 0) {
			statement.setInt(count++, statusId);
		}
		if (!applicantParam.isEmpty()) {
			statement.setString(count++, applicantParam);
		}
		if (!titleParam.isEmpty()) {
			statement.setString(count++, titleParam);
		}
	}
}
