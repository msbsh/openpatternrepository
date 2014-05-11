
package nl.rug.search.opr.controller;

import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@ManagedBean
@RequestScoped
public class DeployInformationController {

    private static Date presumableDeployDate = new Date();

    public Date getPresumableDeployDate() {
        return presumableDeployDate;
    }
}
