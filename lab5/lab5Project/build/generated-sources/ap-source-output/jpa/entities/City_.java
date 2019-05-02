package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Country;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-04-29T15:03:31")
@StaticMetamodel(City.class)
public class City_ { 

    public static volatile SingularAttribute<City, Country> countryCode;
    public static volatile SingularAttribute<City, String> district;
    public static volatile SingularAttribute<City, String> name;
    public static volatile SingularAttribute<City, Integer> id;
    public static volatile SingularAttribute<City, Integer> population;

}