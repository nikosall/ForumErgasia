package gr.metaptixiako.forumergasia;

/**
 * Created by nikos on 1/3/18.
 */

public class QueryInsert {

    String queryforums,querytopics,queryposts;
    public String QueryInsertForums(){

        queryforums = "INSERT INTO forums (name , parent_id ) VALUES ('testforum',1),('testforum2',1)," +
                "('testforum3',1);";


        return queryforums;

    }

    public String QueryInsertTopics(){

        querytopics = "INSERT INTO topics (name , parent_id ) VALUES ('testtopic forum1',1),('testtopic2 forum1',1),('testtopic3 forum1',1),('testtopic forum2',2),('testtopic forum3',3);";


        return querytopics;

    }
    public String QueryInsertPosts(){

        queryposts = "INSERT INTO posts (text , parent_id , author_id ) VALUES ('testpost',1,1);";


        return queryposts;

    }

}
