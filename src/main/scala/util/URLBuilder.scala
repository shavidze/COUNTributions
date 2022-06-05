package util

import model.{ContributorsURL, RepositoriesURL}

object URLBuilder {
  def buildOrganizationURL(organization: String, page: Int = 1): RepositoriesURL = RepositoriesURL(organization, page)

  def buildContributorsURL(organization: String, repositoryName: String, page: Int = 1): ContributorsURL = ContributorsURL(organization, repositoryName, page)
}
