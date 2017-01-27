<jsp:useBean id="elementIndexing" scope="session" class="fr.paris.lutece.plugins.grustorageelastic.web.admin.ElementIndexingJspBean" />
<% String strContent = elementIndexing.processController ( request , response ); %>

<%@ page errorPage="../../../ErrorPage.jsp" %>
<jsp:include page="../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../AdminFooter.jsp" %>