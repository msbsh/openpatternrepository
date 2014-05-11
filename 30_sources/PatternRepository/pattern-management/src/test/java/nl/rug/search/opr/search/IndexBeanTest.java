package nl.rug.search.opr.search;

import java.util.LinkedList;
import java.util.List;
import nl.rug.search.opr.Ejb;
import nl.rug.search.opr.EJBTestBase;
import nl.rug.search.opr.search.mock.MockIndexingTask;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Georg Fleischer
 */
public class IndexBeanTest extends EJBTestBase {

    private IndexLocal indexBean;
    private MockIndexingTask successfulTask;
    private List<MockIndexingTask> failingTasks;

    @Before
    public void setUp() {
        indexBean = Ejb.createIndexBean();
        successfulTask = new MockIndexingTask(false);

        failingTasks = new LinkedList<MockIndexingTask>();
        failingTasks.add(new MockIndexingTask(true));
        failingTasks.add(new MockIndexingTask(true));
        failingTasks.add(new MockIndexingTask(true));
        failingTasks.add(new MockIndexingTask(true));
        failingTasks.add(new MockIndexingTask(true));
    }

    @After
    public void tearDown() {
        indexBean.resetIndexing();
        indexBean = null;
    }

    @Test
    public void canCreateInstance() {
        // then
        assertThat(indexBean.isRebuildIndexRequired(), is(true));
    }

    @Test
    public void canResetRebuildIndexRequired() {
        // when
        indexBean.resetRebuildIndexRequired();

        // then
        assertThat(indexBean.isRebuildIndexRequired(), is(false));
    }

    @Test
    public void canIndexFailingTask(){
        // given
        MockIndexingTask failingTask = failingTasks.get(0);

        // when
        indexBean.indexTask(failingTask);
        failingTask.resetTimesExecuted();

        // then
        indexBean.ejbTimeout(null);
        assertThat(indexBean.isRebuildIndexRequired(), is(false));
        indexBean.ejbTimeout(null);
        assertThat(indexBean.isRebuildIndexRequired(), is(false));
        indexBean.ejbTimeout(null);
        assertThat(indexBean.isRebuildIndexRequired(), is(true));
        indexBean.ejbTimeout(null);

        assertThat(failingTask.getTimesExecuted(), is(3));
    }

    @Test
    public void canRunSuccesfulTask(){
        // given

        // when
        indexBean.indexTask(successfulTask);

        // then
        assertThat(successfulTask.getTimesExecuted(), is(1));

        // when
        indexBean.ejbTimeout(null);

        // then
        assertThat(successfulTask.getTimesExecuted(), is(1));
    }

    @Test
    public void canDiscardSeriousFailingTask(){
        // given
        MockIndexingTask seriousFailure = new MockIndexingTask(false, true);

        // when
        indexBean.indexTask(seriousFailure);
        seriousFailure.resetTimesExecuted();

        indexBean.ejbTimeout(null);

        // then
        assertThat(seriousFailure.getTimesExecuted(), is(0));
    }

    @Test
    public void canRestoreIndex() {
        // given
        for (MockIndexingTask failingTask : failingTasks) {
            indexBean.indexTask(failingTask);
            failingTask.resetTimesExecuted();
            assertThat(failingTask.getTimesExecuted(), is(0));
        }

        // when
        indexBean.ejbTimeout(null);

        // then
        for (MockIndexingTask failingTask : failingTasks) {
            assertThat(failingTask.getTimesExecuted(), is(1));
        }
    }
}
