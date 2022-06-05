object URLBuilder {
  def buildOrganizationURL(organization: String, page: Int = 1): String = s"https://api.github.com/orgs/$organization/repos?per_page=100&page=$page"

  def buildContributorsURL(organization: String, repositoryName: String, page: Int): String = s"https://api.github.com/repos/$organization/$repositoryName/contributors?per_page=100&page=$page"
}