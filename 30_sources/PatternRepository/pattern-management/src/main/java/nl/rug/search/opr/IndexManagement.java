/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.opr;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TimedObject;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import nl.rug.search.opr.entities.pattern.Pattern;
import nl.rug.search.opr.pattern.PatternLocal;
import nl.rug.search.opr.search.SearchLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Georg Fleischer
 */
@Startup
@Singleton
@LocalBean
public class IndexManagement implements TimedObject{

    private static long TIMEOUT_REBUILD_INDEX_CHECK = 20000;
    private static final Logger logger = LoggerFactory.getLogger(IndexManagement.class);
    
    @EJB
    private PatternLocal patternBean;
    @EJB
    private SearchLocal searchBean;
    @Resource
    private TimerService timerService;

    @PostConstruct
    public void onStartup() {
        if (Configuration.getInstance().isProperty(
                ConfigConstants.AUTO_REBUILD_INDEX, true) == false){
            logger.warn("Index management was disabled by configuration");
            return;
        }
        logger.info("Index management is starting up");
        rebuildIndex();

        timerService.createTimer(TIMEOUT_REBUILD_INDEX_CHECK, null);
    }

    @Override
    public void ejbTimeout(Timer timer) {
        if (searchBean.isRebuildIndexRequired()){
            rebuildIndex();
        }
    }

    private void rebuildIndex() {
        logger.info("Rebuilding index");
        List<Pattern> allPatterns = patternBean.getAll();
        searchBean.buildIndex(allPatterns);
    }
}
