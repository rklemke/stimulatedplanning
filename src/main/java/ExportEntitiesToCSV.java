

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;

import stimulatedplanning.PersistentStore;
import stimulatedplanning.UserProfile;

/**
 * Servlet implementation class ExportEntitiesToCSV
 */
@WebServlet("/ExportEntitiesToCSV")
public class ExportEntitiesToCSV extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExportEntitiesToCSV() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String entityType = request.getParameter("entityType");
		
		if (entityType == null || "".equals(entityType) || UserProfile.class.getName().equals(entityType)) {
			response.getWriter().append("Forbidden.");
			return;
		}
		
		PreparedQuery pq = PersistentStore.getEntitiesOfType(entityType);
		
		HashSet<String> props = new HashSet<>();
		
		for (Entity entity : pq.asIterable()) {
			Set<String> set = entity.getProperties().keySet();
			props.addAll(set);
		}
		
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "inline; filename=" + entityType + ".csv");

		String head = "entityType";
		for (String prop : props) {
			head += "; " + prop;
		}
		response.getWriter().append(head + "\n");
		
		for (Entity entity : pq.asIterable()) {
			
			String values = "\""+entity.getKind()+"\"";
			for (String prop : props) {
				String val = PersistentStore.readStringProperty(entity, prop, "");
				if (val != null) {
					val = val.replace("\"", "\\\"");
					val = val.replace("\r", " ");
					val = val.replace("\n", " ");
					val = val.replace(";", ",");
				}
				values += "; \"" + val + "\"";
			}
			response.getWriter().append(values + "\n");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
