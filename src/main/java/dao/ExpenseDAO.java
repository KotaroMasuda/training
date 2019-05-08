package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Expense;
import beans.Status;

/**
 * 社員データを扱うDAO
 */
public class ExpenseDAO {
	/**
	 * クエリ文字列
	 */
	private static final String SELECT_ALL_QUERY = "select \n" + "e.ID as ID, \n" + "e.EXPID, \n" + "e.APPDATE, \n"
			+ "e.CHANDATE, \n" + "e.APPLICANT, \n" + "e.TITLE, \n" + "e.PAYEE, \n" + "e.PRICE, \n" + "e.STATUSID, \n"
			+ "s.TYPE as STATUS_TYPE, \n" + "e.CHANGER from EXPENSE e, STATUS s ";
	private static final String SELECT_BY_ID_QUERY = SELECT_ALL_QUERY + " WHERE e.STATUSID=s.ID and e.ID = ?";
	private static final String INSERT_QUERY = "INSERT INTO "
			+ "EXPENSE(EXPID, APPDATE, CHANDATE, APPLICANT, TITLE, PAYEE, PRICE, STATUSID, CHANGER) "
			+ "VALUES(?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE_QUERY = "UPDATE EXPENSE "
			+ "SET EXPID=?,APPDATE=?,CHANDATE=?,APPLICANT=?,TITLE=?,PAYEE=?,PRICE=?,"
			+ "CHANGER=? WHERE ID = ?";
	private static final String DELETE_QUERY = "DELETE FROM EXPENSE WHERE ID = ?";

	/**
	 * ID指定の検索を実施する。
	 *
	 * @param id
	 *            検索対象のID
	 * @return 検索できた場合は検索結果データを収めたPostインスタンス。検索に失敗した場合はnullが返る。
	 */
	public Expense findById(int id) {
		Expense result = null;

		Connection connection = ConnectionProvider.getConnection();
		if (connection == null) {
			return result;
		}

		try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
			statement.setInt(1, id);

			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				result = processRow(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionProvider.close(connection);
		}

		return result;
	}

	/**
	 * パラメータ指定の検索を実施する。 有効なパラメータ指定が1つも存在しない場合は全件検索になる。
	 *
	 * @param paramExp
	 *            検索用のパラメータを収めたオブジェクト。
	 * @return 検索結果を収めたList。検索結果が存在しない場合は長さ0のリストが返る。
	 */
	public List<Expense> findByParamExp(ParamExp paramExp) {
		List<Expense> result = new ArrayList<>();

		Connection connection = ConnectionProvider.getConnection();
		if (connection == null) {
			return result;
		}

		String queryString = SELECT_ALL_QUERY + paramExp.getWhereClause();
		try (PreparedStatement statement = connection.prepareStatement(queryString)) {
			paramExp.setParameter(statement);

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				result.add(processRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionProvider.close(connection);
		}

		return result;
	}

	/**
	 * 指定されたEmployeeオブジェクトを新規にDBに登録する。 登録されたオブジェクトにはDB上のIDが上書きされる。
	 * 何らかの理由で登録に失敗した場合、IDがセットされない状態（=0）で返却される。
	 *
	 * @param Employee
	 *            登録対象オブジェクト
	 * @return DB上のIDがセットされたオブジェクト
	 */
	public Expense create(Expense expense) {
		Connection connection = ConnectionProvider.getConnection();
		if (connection == null) {
			return expense;
		}

		try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, new String[] { "ID" });) {
			// INSERT実行
			setParameter(statement, expense, false);
			statement.executeUpdate();

			// INSERTできたらKEYを取得
			ResultSet rs = statement.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			expense.setId(id);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			ConnectionProvider.close(connection);
		}

		return expense;
	}

	/**
	 * 指定されたEmployeeオブジェクトを使ってDBを更新する。
	 *
	 * @param expense
	 *            更新対象オブジェクト
	 * @return 更新に成功したらtrue、失敗したらfalse
	 */
	public Expense update(Expense expense) {
		Connection connection = ConnectionProvider.getConnection();
		if (connection == null) {
			return expense;
		}

		try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
			setParameter(statement, expense, true);
			statement.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			ConnectionProvider.close(connection);
		}

		return expense;
	}

	/**
	 * 指定されたIDのPostデータを削除する。
	 *
	 * @param id
	 *            削除対象のPostデータのID
	 * @return 削除が成功したらtrue、失敗したらfalse
	 */
	public boolean remove(int id) {
		Connection connection = ConnectionProvider.getConnection();
		if (connection == null) {
			return false;
		}

		int count = 0;
		try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
			// DELETE実行
			statement.setInt(1, id);
			count = statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionProvider.close(connection);
		}
		return count == 1;
	}

	/**
	 * 検索結果からオブジェクトを復元する。
	 *
	 * @param rs
	 *            検索結果が収められているResultSet。rs.next()がtrueであることが前提。
	 * @return 検索結果を収めたオブジェクト
	 * @throws SQLException
	 *             検索結果取得中に何らかの問題が発生した場合に送出される。
	 */
	private Expense processRow(ResultSet rs) throws SQLException {
		Expense result = new Expense();

		// Employee本体の再現
		result.setId(rs.getInt("ID"));
		result.setExpenseId(rs.getString("EXPID"));
		result.setApplicant(rs.getString("APPLICANT"));
		result.setTitle(rs.getString("TITLE"));
		result.setPayee(rs.getString("PAYEE"));
		result.setPrice(rs.getInt("PRICE")); // Photoオブジェクトに関しては必要になるまで生成しない
		result.setChanger(rs.getString("CHANGER"));

		Date applyDate = rs.getDate("APPDATE");
		if (applyDate != null) {
			result.setApplyDate(applyDate.toString());
		}
		Date changeDate = rs.getDate("CHANDATE");
		if (changeDate != null) {
			result.setChangeDate(changeDate.toString());
		}

		// 入れ子のオブジェクトの再現
		Status status = new Status();
		status.setId(rs.getInt("STATUSID"));
		status.setType(rs.getString("STATUS_TYPE"));
		result.setStatus(status);

		return result;
	}

	/**
	 * オブジェクトからSQLにパラメータを展開する。
	 *
	 * @param statement
	 *            パラメータ展開対象のSQL
	 * @param expense
	 *            パラメータに対して実際の値を供給するオブジェクト
	 * @param forUpdate
	 *            更新に使われるならtrueを、新規追加に使われるならfalseを指定する。
	 * @throws SQLException
	 *             パラメータ展開時に何らかの問題が発生した場合に送出される。
	 */
	private void setParameter(PreparedStatement statement, Expense expense, boolean forUpdate) throws SQLException {
		int count = 1;

		statement.setString(count++, expense.getExpenseId());
		statement.setString(count++, expense.getApplicant());
		statement.setString(count++, expense.getTitle());
		statement.setString(count++, expense.getPayee());
		statement.setInt(count++, expense.getPrice());
		statement.setInt(count++, expense.getStatus().getId());
		statement.setString(count++, expense.getChanger());
		if (expense.getApplyDate() != null) {
			statement.setDate(count++, Date.valueOf(expense.getApplyDate()));
		} else {
			statement.setDate(count++, null);
		}
		if (expense.getChangeDate() != null) {
			statement.setDate(count++, Date.valueOf(expense.getChangeDate()));
		} else {
			statement.setDate(count++, null);
		}

		if (forUpdate) {
			statement.setInt(count++, expense.getId());
		}
	}
}
