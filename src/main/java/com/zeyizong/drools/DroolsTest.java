package com.zeyizong.drools;

import com.zeyizong.drools.model.Product;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.definition.KnowledgePackage;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.util.Collection;

/**
 * Created by yangsen1 on 2017/12/28.
 */
public class DroolsTest {
    public static void main(String[] args) {
        DroolsTest test = new DroolsTest();
//        test.oldExecuteDrools();
        test.testRules();
    }

    private void oldExecuteDrools() {

        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("com/rules/Rules.drl",
                this.getClass()), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            System.out.println(kbuilder.getErrors().toString());
        }

        Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
        // add the package to a rulebase
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        // 将KnowledgePackage集合添加到KnowledgeBase当中
        kbase.addKnowledgePackages(pkgs);

        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        Product product = new Product();
        product.setType(Product.GOLD);
        ksession.insert(product);
        ksession.fireAllRules();
        ksession.dispose();

        System.out.println("The discount for the product " + product.getType()
                + " is " + product.getDiscount()+"%");
    }

    private void testRules(){
        //构建KieServices
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession("ksession-rule");

        Product product = new Product();
        product.setType(Product.GOLD);

        kieSession.insert(product);

        int count = kieSession.fireAllRules();
        System.out.println("命中了"+count+"条规则");
        System.out.println("商品"+product.getType()+"的商品折扣为"+product.getDiscount()+"%");
    }
}
