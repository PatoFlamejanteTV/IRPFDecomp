package dirpf2025scriptlets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

public class RelDeclaracaoRendimentosTributaveisPJDependenteScriptlet extends JRDefaultScriptlet {
   private Map<String, Double> colecao = new HashMap();

   public void afterDetailEval() throws JRScriptletException {
      super.afterDetailEval();
      Object localObject1 = 0.0D;
      Object localObject2 = "";
      String niFontePagadora = (String)this.getFieldValue("NIFontePagadora");
      Double localDouble1 = (Double)this.getFieldValue("rendRecebidoPJ");
      if (niFontePagadora != null && !niFontePagadora.trim().equals("")) {
         if (localDouble1 == null) {
            localDouble1 = 0.0D;
         }

         localObject2 = niFontePagadora;
         localObject1 = localDouble1;
         Double localDouble2 = (Double)this.colecao.get(niFontePagadora);
         if (localDouble2 == null) {
            this.colecao.put(niFontePagadora, localDouble1);
         } else {
            this.colecao.put(niFontePagadora, localDouble2 + localDouble1);
         }

         Set<String> chaves = this.colecao.keySet();
         niFontePagadora = "";
         localDouble1 = 0.0D;
         Iterator itChaves = chaves.iterator();

         while(itChaves.hasNext()) {
            niFontePagadora = (String)itChaves.next();
            localDouble1 = (Double)this.colecao.get(niFontePagadora);
            if (localDouble1 > (Double)localObject1) {
               localObject1 = localDouble1;
               localObject2 = niFontePagadora;
            }
         }

         this.setVariableValue("niMaiorFontePagadora", localObject2);
      }

   }
}
