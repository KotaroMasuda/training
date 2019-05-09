package beans;

public class Login {

		private String loginId;
		private String userName;
		private UserType userTpe;
		private LoginStatus loginStatus;

		public String getLoginId() {
			return loginId;
		}
		public void setLoginId(String loginId) {
			this.loginId = loginId;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public UserType getUserTpe() {
			return userTpe;
		}
		public void setUserTpeByInt(int  userTpeAsInt) {
			if(userTpeAsInt == 0){
				this.userTpe = UserType.STANDARD;
			}else if(userTpeAsInt == 1){
				this.userTpe = UserType.AUTHORIZED;
			}
		}
		public LoginStatus getLoginStatus() {
			return loginStatus;
		}
		public void setLoginStatusByInt(int loginStatusAsInt) {
			if(loginStatusAsInt == 0){
				this.loginStatus = LoginStatus.LOGOUT;
			}else if(loginStatusAsInt == 1){
				this.loginStatus = LoginStatus.LOGIN;
			}
		}

		@Override
		public String toString(){
			StringBuffer sb = new StringBuffer();
			sb.append(this.getLoginId());
			sb.append(",");
			sb.append(this.getUserName());
			sb.append(",");
			sb.append(this.getUserTpe());
			sb.append(",");
			sb.append(this.getLoginStatus());
			sb.append(",");

			return sb.toString();
		}
}
