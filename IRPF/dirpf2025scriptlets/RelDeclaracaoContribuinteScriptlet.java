package dirpf2025scriptlets;

import net.sf.jasperreports.engine.JRDefaultScriptlet;

public class RelDeclaracaoContribuinteScriptlet extends JRDefaultScriptlet {
   public String calcularModulo11(String stEnt, String stMult, int inStatus) {
      int inSoma = 0;
      int valor;
      if (stMult == null) {
         for(valor = 0; valor < stEnt.length(); ++valor) {
            inSoma += (stEnt.length() + 1 - valor) * Character.getNumericValue(stEnt.charAt(valor));
         }
      } else {
         for(valor = 0; valor < stEnt.length(); ++valor) {
            inSoma += Character.getNumericValue(stMult.charAt(valor)) * Character.getNumericValue(stEnt.charAt(valor));
         }
      }

      int inResto = inSoma - inSoma / 11 * 11;
      int inMod11 = 11 - inResto;
      if (inStatus == 2) {
         if (inMod11 > 9) {
            inMod11 = 0;
         }
      } else if (inStatus == 1) {
         if (inMod11 == 10) {
            inMod11 = 0;
         }

         if (inMod11 == 11) {
            inMod11 = 1;
         }
      }

      valor = this.calcularModulo11B(stEnt + inMod11, (String)null, 2);
      String resultado = inMod11 + String.valueOf(valor);
      return resultado;
   }

   public int calcularModulo11B(String stEnt, String stMult, int inStatus) {
      int inSoma = 0;
      int i;
      if (stMult == null) {
         for(i = 0; i < stEnt.length(); ++i) {
            inSoma += (stEnt.length() + 1 - i) * Character.getNumericValue(stEnt.charAt(i));
         }
      } else {
         for(i = 0; i < stEnt.length(); ++i) {
            inSoma += Character.getNumericValue(stMult.charAt(i)) * Character.getNumericValue(stEnt.charAt(i));
         }
      }

      int inResto = inSoma - inSoma / 11 * 11;
      int inMod11 = 11 - inResto;
      if (inStatus == 2) {
         if (inMod11 > 9) {
            inMod11 = 0;
         }
      } else if (inStatus == 1) {
         if (inMod11 == 10) {
            inMod11 = 0;
         }

         if (inMod11 == 11) {
            inMod11 = 1;
         }
      }

      return inMod11;
   }
}
