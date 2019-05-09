package java_s04;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import beans.Expense;
import beans.Status;
import dao.ExpenseDAO;
import dao.ParamExp;
import dao.StatusDAO;

/**
 * 従業員関連のサービス実装。
 * Servlet/JSPの実装とは異なり、画像についてはバイナリでなくpathベースで扱うものとする。
 */
@Path("expenses")
public class ExpenseResource {
//	private final EmployeeDAO empDao = new EmployeeDAO();
//	private final PostDAO postDao = new PostDAO();
//	private final PhotoDAO photoDao = new PhotoDAO();
	private final ExpenseDAO expDao = new ExpenseDAO();
	private final StatusDAO statusDao = new StatusDAO();

	/**
	 * ID指定で従業員情報を取得する。
	 *
	 * @param id 取得対象の従業員のID
	 * @return 取得した従業員情報をJSON形式で返す。データが存在しない場合は空のオブジェクトが返る。
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Expense findById(@PathParam("id") int id) {
		return expDao.findById(id);
	}

	/**
	 * クエリパラメータ指定による検索を実施する。
	 * 何も指定しない場合は全件検索になる。
	 *
	 * @param postId 部署ID。指定しない場合は0が入る。
	 * @param empId ログイン用の従業員ID。指定しない場合はnullが入る。
	 * @param nameParam 名前の一部を指定するためのパラメータ。指定しない場合はnullが入る。
	 * @return 取得した従業員情報のリストをJSON形式で返す。データが存在しない場合は空のオブジェクトが返る。
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Expense> findByParamExp(
			@QueryParam("statusId") int statusId,
			@QueryParam("applicantParam") String applicantParam,
			@QueryParam("titleParam") String titleParam) {
		ParamExp paramExp = new ParamExp(statusId, applicantParam, titleParam);
		return expDao.findByParamExp(paramExp);
	}

	/**
	 * 指定した従業員情報を登録する。
	 *
	 * @param form 従業員情報（画像含む）を収めたオブジェクト
	 * @return DB上のIDが振られた従業員情報
	 * @throws WebApplicationException 入力データチェックに失敗した場合に送出される。
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Expense create(final FormDataMultiPart form) throws WebApplicationException {
		Expense expense = new Expense();

		expense.setId(0);
		expense.setExpenseId(form.getField("expenseId").getValue());
		expense.setApplicant(form.getField("applicant").getValue());
		expense.setTitle((form.getField("title").getValue()));
		expense.setPayee((form.getField("payee").getValue()));
		expense.setPrice(Integer.parseInt(form.getField("price").getValue()));
//		expense.setChanger((form.getField("changer").getValue()));

		String applyDateStr = form.getField("applyDate").getValue();
		if (applyDateStr != null && !applyDateStr.isEmpty()) {
			expense.setApplyDate(applyDateStr);
		}

//		String changeDateStr = form.getField("changeDate").getValue();
//		if (changeDateStr != null && !changeDateStr.isEmpty()) {
//			expense.setChangeDate(changeDateStr);
//		}

//		if (!expense.isValidObject()) {
//			throw new WebApplicationException(Response.Status.BAD_REQUEST);
//		}

		//Status関連の処理
//		int statusId = Integer.parseInt(form.getField("statusId").getValue());
//		beans.Status status = statusDao.findById(statusId);
//		if (status == null) {
//			throw new WebApplicationException(Response.Status.BAD_REQUEST);
//		}
		Status status = new Status();
		status.setId(1);
		expense.setStatus(status);

		return expDao.create(expense);
	}

	/**
	 * 指定した情報でDBを更新する。
	 *
	 * @param form 更新情報を含めた従業員情報
	 * @throws WebApplicationException 入力データチェックに失敗した場合に送出される。
	 */
	@PUT
	@Path("{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Expense update(@PathParam("id") int id,
			final FormDataMultiPart form) throws WebApplicationException {
		Expense expense = new Expense();

		expense.setId(id);
		expense.setExpenseId(form.getField("expenseId").getValue());
		expense.setApplicant(form.getField("applicant").getValue());
		expense.setTitle((form.getField("title").getValue()));
		expense.setPayee((form.getField("payee").getValue()));
		expense.setPrice(Integer.parseInt(form.getField("price").getValue()));
		expense.setChanger((form.getField("changer").getValue()));

		String applyDateStr = form.getField("applyDate").getValue();
		if (applyDateStr != null && !applyDateStr.isEmpty()) {
			expense.setApplyDate(applyDateStr);
		}

		String changeDateStr = form.getField("changeDate").getValue();
		if (changeDateStr != null && !changeDateStr.isEmpty()) {
			expense.setChangeDate(changeDateStr);
		}

		if (!expense.isValidObject()) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

		// Status関連の処理
		int statusId = Integer.parseInt(form.getField("statusId").getValue());
		beans.Status status = statusDao.findById(statusId);
		if (status == null) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		expense.setStatus(status);

		return expDao.update(expense);
	}

	/**
	 * 指定したIDの社員情報を削除する。同時に画像データも削除する。
	 *
	 * @param id 削除対象の社員情報のID
	 */
	@DELETE
	@Path("{id}")
	public void remove(@PathParam("id") int id) {
		Expense expense = expDao.findById(id);
		expDao.remove(id);
	}

//	@GET
//	@Path("csv")
//	@Produces(MediaType.APPLICATION_OCTET_STREAM)
//	public Response downloadCsv() {
//		Param param = new Param(0, "", "");
//		List<Expense> list = expDao.findByParam(param);
//
//		String header = "ID,社員番号,名前,年齢,性別,写真ID,郵便番号,都道府県,住所,所属部署ID,入社日付,退社日付"
//				+ System.getProperty("line.separator");
//		StringBuffer csvContents = new StringBuffer(header);
//
//		for (Expense employee : list) {
//			String line = employee.toString() + System.getProperty("line.separator");
//			csvContents.append(line);
//		}
//
//		return Response.status(Status.OK)
//				.entity(csvContents.toString())
//				.header("Content-disposition", "attachment; filename=employee.csv")
//				.build();
//	}
//
//	/**
//	 * Formから渡されたデータを使用してPhotoデータを登録する。
//	 *
//	 * @param photoPart Formから渡されたPhotoデータ
//	 * @return 登録されてIDが振られたPhotoインスタンス
//	 */
//	private Photo createPhoto(FormDataBodyPart photoPart) {
//		Photo photo = build(photoPart);
//
//		return photoDao.create(photo);
//	}
//
//	/**
//	 * Formから渡されたデータを使用してPhotoデータを更新する。
//	 *
//	 * @param photoId 更新対象のPhotoのID
//	 * @param photoPart Formから渡されたPhotoデータ
//	 * @return 正常に更新された場合はtrue、失敗した場合はfalse
//	 */
//	private boolean updatePhoto(int photoId, FormDataBodyPart photoPart) {
//		Photo photo = build(photoPart);
//		photo.setId(photoId);
//		return photoDao.update(photo);
//	}
//
//	/**
//	 * formから渡されたデータを使用してPhotoインスタンスを構築する。
//	 *
//	 * @param photoPart Formから渡されたPhotoデータ
//	 * @return ID以外のフィールドに値がセットされたPhotoインスタンス
//	 */
//	private Photo build(FormDataBodyPart photoPart) {
//		Photo photo = new Photo();
//		ContentDisposition photoInfo = photoPart.getContentDisposition();
//
//		photo.setFileName(photoInfo.getFileName());
//
//		photo.setContentType(photoPart.getMediaType().toString());
//
//		BodyPartEntity bodyPartEntity = (BodyPartEntity)photoPart.getEntity();
//		InputStream in = bodyPartEntity.getInputStream();
//		photo.setPhoto(in);
//
//		photo.setEntryDate(new Date(System.currentTimeMillis()));
//		return photo;
//	}
}
