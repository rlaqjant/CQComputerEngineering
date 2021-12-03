package cq.ce.four;

import org.kohsuke.github.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 깃헙 이슈 1번부터 18번까지 댓글을 순회하며 댓글을 남긴 사용자를 체크 할 것.
 * 참여율을 계산하세요. 총 18회에 중에 몇 %를 참여했는지 소숫점 두자리가지 보여줄 것.
 * Github 자바 라이브러리를 사용하면 편리합니다.
 * 깃헙 API를 익명으로 호출하는데 제한이 있기 때문에 본인의 깃헙 프로젝트에 이슈를 만들고 테스트를 하시면 더 자주 테스트할 수 있습니다.
 *
 * */
public class LiveStudyDashboard
{
    /**사용자별 참여율 구하기*/
    public static void participationRate() throws Exception
    {
        List<GHIssue> issues = getIssues();
        Set<String> users = getCommentUsers(issues);

        int i = issues.size();
        int c = 0;

        System.out.println("전체 과제 수 : " + i + "개 / " + "참여 인원 : " + users.size() + "명");
        for (String user : users)
        {
            for (GHIssue issue : issues)
            {
                if(issue.getComments().stream().anyMatch(a -> {
                    try
                    {
                        return a.getUser().getLogin().equals(user);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        return false;
                    }
                }))
                    c++;
            }
            float participationRate = (c*100)/i;
            System.out.println("사용자 "+user+"의 참여율은 "+String.format("%.2f",participationRate)+"% 입니다.");
        }


        System.out.println();

    }

    /**이슈 불러오기*/
    public static List<GHIssue> getIssues() throws IOException
    {
        GitHub gitHub = new GitHubBuilder().withOAuthToken("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").build();

        GHRepository repo = gitHub.getRepository("rlaqjant/CQComputerEngineering");
        //GHRepository repo = gitHub.getRepository("whiteship/live-study");

        return repo.getIssues(GHIssueState.ALL);
    }

    /**댓글 단 사람들 불러오기
     * @param issues*/
    public static Set<String> getCommentUsers(List<GHIssue> issues) throws IOException
    {
        Set<String> users = new HashSet<>();

        for (GHIssue issue : issues)
        {
            for (GHIssueComment comment : issue.getComments())
            {
                users.add(comment.getUser().getLogin());
            }
        }

        return users;
    }

}
