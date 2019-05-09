package java_s04;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import dao.LoginDAO;


/**
 * Servlet implementation class ItemSearchServlet
 */
@Path("login")
public class LoginResource {
	private final LoginDAO loginDao = new LoginDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@POST
	public beans.Login check(final FormDataMultiPart form) throws WebApplicationException {
		beans.Login login = new beans.Login();

		login.setLoginId(form.getField("loginId").getValue());
		login.setUserName(form.getField("userName").getValue());
		String userType = form.getField("userType").getValue();
		String loginStatus = form.getField("loginStatus").getValue();

		return loginDao.trial(login);
	}
}
