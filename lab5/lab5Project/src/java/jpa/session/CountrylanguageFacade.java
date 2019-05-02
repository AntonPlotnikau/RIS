/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.entities.Countrylanguage;

/**
 *
 * @author User
 */
@Stateless
public class CountrylanguageFacade extends AbstractFacade<Countrylanguage> {
    @PersistenceContext(unitName = "lab5ProjectPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CountrylanguageFacade() {
        super(Countrylanguage.class);
    }
    
}
