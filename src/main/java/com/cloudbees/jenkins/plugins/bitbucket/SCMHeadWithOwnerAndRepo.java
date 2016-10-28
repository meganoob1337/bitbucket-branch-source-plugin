/*
 * The MIT License
 *
 * Copyright (c) 2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cloudbees.jenkins.plugins.bitbucket;

import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequestSource;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.ObjectStreamException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequest;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import hudson.model.Action;
import jenkins.scm.api.SCMHead;

/**
 * {@link SCMHead} extended with additional information:
 * <ul>
 *   <li>{@link #repoOwner}: the repository owner</li>
 *   <li>{@link #repoName}: the repository name</li>
 *   <li>{@link #metadata}: metadata related to Pull Requests - null if this object is not representing a PR</li>
 * </ul>
 * This information is required in this plugin since {@link BitbucketSCMSource} is processing pull requests
 * and they are managed as separate repositories in Bitbucket without any reference to them in the destination
 * repository.
 * @deprecated use {@link PullRequestSCMHead} or {@link BranchSCMHead}
 */
@Deprecated
public class SCMHeadWithOwnerAndRepo extends SCMHead {

    private static final long serialVersionUID = 1L;

    private final String repoOwner;

    private final String repoName;

    private transient PullRequestAction metadata;

    public SCMHeadWithOwnerAndRepo(String repoOwner, String repoName, String branchName) {
        super(branchName);
        this.repoOwner = repoOwner;
        this.repoName = repoName;
    }

    private SCMHead readResolve() throws ObjectStreamException {
        if (metadata != null) {
            return new PullRequestSCMHead(repoOwner, repoName, super.getName(), new BitbucketPullRequest() {
                @Override
                public BitbucketPullRequestSource getSource() {
                    return null;
                }

                @NonNull
                @Override
                public String getId() {
                    return metadata.getId();
                }

                @Override
                public String getTitle() {
                    return metadata.getTitle();
                }

                @Override
                public String getLink() {
                    URL url = metadata.getURL();
                    return url == null ? null : url.toString();
                }

                @Override
                public String getAuthorLogin() {
                    return metadata.getAuthor();
                }
            });
        }
        return new BranchSCMHead(repoOwner, repoName, getName());
    }

}
