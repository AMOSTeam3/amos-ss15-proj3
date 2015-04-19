package connection;


public interface Connector {

	String getLatestCommitMessage(String repositoryUrl) throws Exception;

	String getLatestCommitId(String repositoryUrl) throws Exception;

}
