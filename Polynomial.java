package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author J.T. McQuigg
 * 			jtm258
 * 			199006858
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) 
	{
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node pointerheadpoly1 = poly1;
		Node pointerheadpoly2 = poly2;
		Node pointerhead = new Node(0, 0, null);
		Node Pointernext = pointerhead;

		while((poly1 != null && poly2 !=null)){
			Node newNode = new Node(0, 0, null);
			if(poly1.term.degree == poly2.term.degree){
				newNode.term.coeff = poly1.term.coeff + poly2.term.coeff;
				newNode.term.degree = poly1.term.degree;
				poly1 = poly1.next;
				poly2 = poly2.next;
			}else if(poly1.term.degree < poly2.term.degree){
				newNode.term.coeff = poly1.term.coeff;
				newNode.term.degree = poly1.term.degree;
				poly1 = poly1.next;
			}else if(poly2.term.degree < poly1.term.degree){
				newNode.term.coeff =  poly2.term.coeff;
				newNode.term.degree = poly2.term.degree;		
				poly2 = poly2.next;
			}

			if(newNode.term.coeff == 0){
                continue;
            }
			
			Pointernext.next = newNode;
			Pointernext = Pointernext.next;
		}
		while(poly1 != null || poly2 !=null){
			Node newNode = new Node(0, 0, null);
			if(poly1 != null){	
				newNode.term.coeff = poly1.term.coeff;
				newNode.term.degree = poly1.term.degree;
				poly1 = poly1.next;	
			}else if(poly2 != null){
				newNode.term.coeff = poly2.term.coeff;
				newNode.term.degree = poly2.term.degree;
				poly2 = poly2.next;
			}
			Pointernext.next = newNode;
			Pointernext = Pointernext.next;
		}
		Node check = pointerhead;
		while(check.term.coeff == 0){
			if(check.next == null){
				return null;
			}
			check = check.next;
		}

		poly1 = pointerheadpoly1;
		poly2 = pointerheadpoly2;
		return pointerhead.next;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
        Node pointerhead = new Node(0, 0, null);
        Node Pointernext = pointerhead;
        Node poly1point = poly1;
        Node poly2point = poly2;
		Node Simplify = new Node(0, 0, null);
        //everything in poly 1 times poly 2
        for(int i = 0; poly1point != null; i++){
            for(int j = 0; poly2point != null; j++){
                Node newNode = new Node(0, 0, null);
                newNode.term.coeff = (poly1point.term.coeff * poly2point.term.coeff);
                newNode.term.degree = (poly2point.term.degree + poly1point.term.degree);
                poly2point = poly2point.next;
                Pointernext.next = newNode;
                Pointernext = Pointernext.next;
            }
			Simplify = add(Simplify, pointerhead.next);
            poly1point = poly1point.next;
            poly2point = poly2;
			Pointernext = pointerhead;
        }

        return Simplify;
    }

	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node pointerhead = poly;
		float total = 0;
		float overall = 0;
		while(poly != null){
		total = 0;
		total = (float)Math.pow(x, poly.term.degree);
        total = total * poly.term.coeff;
		overall = overall + total;
		poly = poly.next;
		}
		poly = pointerhead;
		
        return overall;
		
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
