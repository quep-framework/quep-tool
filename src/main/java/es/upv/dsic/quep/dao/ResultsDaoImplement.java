/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.Menu;
import es.upv.dsic.quep.hibernate.HibernateUtil;
import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.Response;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author agna8685
 */
public class ResultsDaoImplement implements ResultsDao {

    @Inject private HibernateUtil hibernateUtil;
           
   @Override
    public List<QuepQuestionResponseOption> getQuepQuestionResponseOption(int idOrg) {
        Session session = null;
        List<QuepQuestionResponseOption> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            //Query query = session.createQuery("select distinct qqq.quepQuestion.practice.principle from QuestionnaireQuepQuestion qqq where qqq.active=1 and qqq.id.idRole='" + idRole + "'");
           /* Query query = session.createQuery("select distinct qqro\n"
                    + "from  QuestionnaireQuepQuestion qqq, QuepQuestionResponseOption qqro,\n"
                    + "RoleStakeholder rs \n"
                    + ", QuestionnaireResponse qr\n"
                    + "where qqq.active=1 and qqro.active=1 and rs.active=1\n"
                    + "and rs.id.idOrganization='" + idOrg + "'" + "\n"
                    + "and qqq.id.idQuepQuestion=qqro.id.idQuepQuestion\n"
                    + "and qqq.questionnaire.role.id=rs.id.idRole\n"
                    
                    + "and qqq.quepQuestion.active=1 \n"
                    +" and qr.id.idQuestionnaire=qqq.id.idQuestionnaire and qr.id.idPractice=qqq.id.idPractice and\n"
                            + " qr.id.idStakeholder=rs.stakeholder.id and qr.id.idRole=rs.role.id and qr.id.idOrganization =rs.id.idOrganization \n" 
                            + "and qr.active=1 and qr.status in (1,2) \n"
                           // + "and qqq.quepQuestion.maturityLevel.id=5 "
                            + "and qqro.responseOption.isRequiered=1\n"
                    
            );*/
           Query query = session.createQuery("select distinct qqro\n"
                    + "from  QuestionnaireQuepQuestion qqq, "
                   + "QuepQuestionResponseOption qqro\n"                    
                    + "where qqq.active=1 and qqro.active=1 \n"                    
                    + "and qqq.id.idQuepQuestion=qqro.id.idQuepQuestion\n"                                        
                    + "and qqq.quepQuestion.active=1 \n"                    
                           //+ "and qqq.quepQuestion.maturityLevel.id=1 \n"
                   //+ "and qqq.quepQuestion.id=1 "
                            + "and qqro.responseOption.isRequiered=1\n"
                    
            );
            list = (List<QuepQuestionResponseOption>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }
   /* public List<QuepQuestionResponseOption> getQuepQuestionResponseOption(int idOrg) {
        Session session = null;
        List<QuepQuestionResponseOption> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            //Query query = session.createQuery("select distinct qqq.quepQuestion.practice.principle from QuestionnaireQuepQuestion qqq where qqq.active=1 and qqq.id.idRole='" + idRole + "'");
            Query query = session.createQuery("select distinct qqro\n"
                    + "from  QuestionnaireQuepQuestion qqq, QuepQuestionResponseOption qqro,\n"
                    + "RoleStakeholder rs \n"
                    + "where qqq.active=1 and qqro.active=1 and rs.active=1\n"
                    + "and rs.id.idOrganization='" + idOrg + "'" + "\n"
                    + "and qqq.id.idQuepQuestion=qqro.id.idQuepQuestion\n"
                    + "and qqq.questionnaire.role.id=rs.id.idRole");
            list = (List<QuepQuestionResponseOption>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }*/

    
      @Override
    public List<Response> getListResponse(int idOrg) {
        Session session = null;
        List<Response> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            /*Query query = session.createQuery("select r\n"
                    + "from Response r \n"
                    + "where r.active=1 \n"
                    //+ "and r.questionnaireQuepQuestion.quepQuestion.maturityLevel.id=5 "
                    + "and r.responseOption.isRequiered=1\n"
                    + " and r.id.idOrganization='" + idOrg + "'");*/
            Query query = session.createQuery("select r\n"
                    + "from Response r \n"
                    + "where r.active=1 \n"
                    + "and r.questionnaireQuepQuestion.quepQuestion.active=1\n"
                    + "and  r.responseOption.active=1\n"
                    + "and r.questionnaireResponse.status=1\n" 
                  //  + "and r.questionnaireQuepQuestion.quepQuestion.maturityLevel.id=1 "
                    //+ " and  r.questionnaireQuepQuestion.quepQuestion.id=1"
                    + "and r.responseOption.isRequiered=1\n"
                    + "and r.id.idOrganization='" + idOrg + "'");
            list = (List<Response>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    @Override
    public int getLegendQrStatus(int idOrg,int iStatus) {
        Session session = null;
        int contStatus=0;
        List list = null;
        
        try {
           session = HibernateUtil.getSessionFactory().openSession();           
           Query query = session.createQuery("select distinct qr.id.idStakeholder,qr.id.idOrganization\n"                   
                   + "from QuestionnaireResponse qr \n"
                   + "where qr.active=1 and status = '" + iStatus + "'"+"\n"
                            + " and qr.id.idOrganization='" + idOrg + "'\n"
                  // + "group by  \n"            
            );
           
           list=   (List) query.list();             
           contStatus= list.size();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return contStatus;
    }
}
