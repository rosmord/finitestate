/*
 * Copyright ou © ou Copr. Serge Rosmorduc (2016) 
 * serge.rosmorduc@cnam.fr

 * Ce logiciel est un programme informatique servant à mettre en place 
 * une base de données linguistique pour l'égyptien ancien.

 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence [CeCILL|CeCILL-B|CeCILL-C] telle que diffusée par le CEA, le CNRS et l'INRIA 
 * sur le site "http://www.cecill.info".

 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.

 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant 
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 

 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 */
package org.qenherkhopeshef.finitestate.lazy.character;

import org.qenherkhopeshef.finitestate.lazy.RegularExtractor;

import java.util.List;

import static org.qenherkhopeshef.finitestate.lazy.RegularLanguageFactory.*;

/**
 * A simple demonstration for fun and profit.
 *
 * @author rosmord
 */
public class DemoText {

    public static void main(String[] args) {
        extractNumbers1();
        extractNumbers2();
        demoTextComplex();
    }

    private static void extractNumbers1() {
        RegularExtractor<Character> rec
                = RegularExtractor.<Character>getBuilder()
                        .part(star(any()))
                        .part(plus(range('0', '9')))
                        .part(exact('.'))
                        .build();
        String s = "dhfjhdfsh0023.dfsd35fds";
        List<Character> toMatch = StringToListHelper.fromString(s);
        rec.recognizesBeginning(toMatch).ifPresent((m) -> {
            System.out.println(m);
        });
    }

    private static void extractNumbers2() {
        String s = "dhfjhdfsh0023.dfsd35fds";
        RegularExtractor<Character> rec1
                = RegularExtractor.<Character>getBuilder()
                        .part(star(any()))
                        .part(plus(range('0', '9')))
                        .part(outOfRange('0', '9'))
                        .build();
        List<Character> toMatch = StringToListHelper.fromString(s);
        rec1.recognizesBeginning(toMatch).ifPresent((m) -> {
            System.out.println(m);
        });
    }

    private static void demoTextComplex() {
        // A demonstration with a complex language :
        // recognise strings starting with 'a' and ending with 'z', except when there
        // is a 'b' inside them.
        RegularExtractor<Character> rec
                = RegularExtractor.<Character>getBuilder()
                        .part(inter(
                                seq(exact('a'), skip(), exact('b')),
                                complement(seq(skip(), exact('z'), skip()))
                        ))
                        .build();
        String s = "xa00z00bxa11bxa22222bx";
        List<List<Integer>> result = rec.search(StringToListHelper.fromString(s));
        for (List<Integer> posList: result) {
            System.out.println(s.substring(posList.get(0), posList.get(1)));
        }
    }
}
