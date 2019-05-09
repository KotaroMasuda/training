package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Login;

public class LoginDAO {

	/**
	 * 指定されたEmployeeオブジェクトを新規にDBに登録する。
	 * 登録されたオブジェクトにはDB上のIDが上書きされる。
	 * 何らかの理由で登録に失敗した場合、IDがセットされない状態（=0）で返却される。
	 *
	 * @param Employee 登録対象オブジェクト
	 * @return DB上のIDがセットされたオブジェクト
	 */
	public Login trial(Login login) {
		Connection connection = ConnectionProvider.getConnection();
		if (connection == null) {
			return login;
		}

		try (PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, new String[] { "ID" });) {
			// INSERT実行
			setParameter(statement, login, false);
			statement.executeUpdate();

			// INSERTできたらKEYを取得
			ResultSet rs = statement.getGeneratedKeys();
			rs.next();
			String loginId = rs.getString(1);
			login.setLoginId(loginId);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			ConnectionProvider.close(connection);
		}

		return login;
	}

	/**
	 * オブジェクトからSQLにパラメータを展開する。
	 *
	 * @param statement パラメータ展開対象のSQL
	 * @param login パラメータに対して実際の値を供給するオブジェクト
	 * @param forUpdate 更新に使われるならtrueを、新規追加に使われるならfalseを指定する。
	 * @throws SQLException パラメータ展開時に何らかの問題が発生した場合に送出される。
	 */
	private void setParameter(PreparedStatement statement, Login login, boolean forUpdate) throws SQLException {
		int count = 1;

		statement.setString(count++, login.getLoginId());
		statement.setString(count++, login.getUserName());

		if (forUpdate) {
			statement.setString(count++, login.getLoginId());
		}

	}

}
