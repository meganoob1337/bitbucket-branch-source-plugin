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

import com.cloudbees.jenkins.plugins.bitbucket.api.BitbucketPullRequest;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.model.Action;
import java.util.LinkedList;
import java.util.List;
import jenkins.scm.api.ChangeRequestSCMHead;
import jenkins.scm.api.SCMHead;
import jenkins.scm.api.actions.ChangeRequestAction;

/**
 * {@link SCMHead} for a BitBucket Pull request
 * @since FIXME
 */
public class PullRequestSCMHead extends ChangeRequestSCMHead implements BitbucketSCMHead {

    private static final String PR_BRANCH_PREFIX = "PR-";

    private static final long serialVersionUID = 1L;

    private final String repoOwner;

    private final String repoName;

    private final String branchName;

    @NonNull
    private PullRequestAction metadata;

    public PullRequestSCMHead(String repoOwner, String repoName, String branchName, BitbucketPullRequest pr) {
        super(PR_BRANCH_PREFIX + pr.getId());
        this.repoOwner = repoOwner;
        this.repoName = repoName;
        this.branchName = branchName;
        this.metadata = new PullRequestAction(pr);
    }

    public String getRepoOwner() {
        return repoOwner;
    }

    public String getRepoName() {
        return repoName;
    }

    @Override
    public String getBranchName() {
        return branchName;
    }

    @CheckForNull
    public Integer getPullRequestId() {
        return Integer.parseInt(metadata.getId());
    }

    @NonNull
    @Override
    public ChangeRequestAction getChangeRequestAction() {
        return metadata;
    }

}
