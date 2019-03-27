/**
 * <p>
 * Trust Boundary Violation is fancy name to describe tainted value passed directly to session attribute.
 * This could be an expected behavior that allow an attacker to change the session state.
 * </p>
 * <p>
 * When the parameter is dynamic, it is a lot more suspicious than when it is a dynamic value.
 * <code>setAttribute( suspiciousValue, "true")</code>
 * vs
 * <code>setAttribute( "language" , commonDynamicValue)</code>
 * </p>
 * <p>
 * For this reason, the trust boundary violation was split in two detectors.
 * This will allow user to hide the low priority of this detector.
 * </p>
 *
 * @see com.h3xstream.findsecbugs.injection.trust.TrustBoundaryViolationAttributeDetector
 * @see com.h3xstream.findsecbugs.injection.trust.TrustBoundaryViolationValueDetector
 */
package com.h3xstream.findsecbugs.injection.trust;