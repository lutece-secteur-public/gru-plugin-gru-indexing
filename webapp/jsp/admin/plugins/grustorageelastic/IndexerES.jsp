<jsp:useBean id="indexing" scope="session" class="fr.paris.lutece.plugins.grustorage.elastic.web.rs.IndexationESJspBean" />
<% String strContent = indexing.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>

