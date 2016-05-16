/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.ext;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * @author Michal Sabo
 */
public class SpringProps {

    public static Props create(ActorSystem actorSystem, String actorBeanName) {
        return SpringExtension.instance().get(actorSystem).create(actorBeanName);
    }

    public static Props create(ActorSystem actorSystem, Class<? extends UntypedActor> requiredType) {
        return SpringExtension.instance().get(actorSystem).create(requiredType);
    }

    public static Props create(ActorSystem actorSystem, String actorBeanName, Class<? extends UntypedActor> requiredType) {
        return SpringExtension.instance().get(actorSystem).create(actorBeanName, requiredType);
    }
}
