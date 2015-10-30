/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.dmn.engine.impl.hitpolicy;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.dmn.engine.hitpolicy.DmnHitPolicyAggregator;
import org.camunda.bpm.dmn.engine.impl.DmnLogger;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.TypedValue;

public abstract class AbstractDmnHitPolicyNumberAggregator implements DmnHitPolicyAggregator {

  public static final DmnHitPolicyLogger LOG = DmnLogger.HIT_POLICY_LOGGER;

  public TypedValue aggregate(List<TypedValue> outputValues) {
    if (outputValues.isEmpty()) {
      // return null if no values to aggregate
      return null;
    }
    else {

      return aggregateNumberValues(outputValues);
    }
  }

  protected TypedValue aggregateNumberValues(List<TypedValue> values) {
    try {
      List<Integer> intValues = convertValuesToInteger(values);
      return Variables.integerValue(aggregateIntegerValues(intValues));
    }
    catch (IllegalArgumentException e) {
      // ignore
    }

    try {
      List<Long> longValues = convertValuesToLong(values);
      return Variables.longValue(aggregateLongValues(longValues));
    }
    catch (IllegalArgumentException e) {
      // ignore
    }

    try {
      List<Double> doubleValues = convertValuesToDouble(values);
      return Variables.doubleValue(aggregateDoubleValues(doubleValues));
    }
    catch (IllegalArgumentException e) {
      // ignore
    }

    throw LOG.unableToConvertValuesToAggregatableTypes(values, Integer.class, Long.class, Double.class);
  }

  protected abstract Integer aggregateIntegerValues(List<Integer> intValues);

  protected abstract Long aggregateLongValues(List<Long> longValues);

  protected abstract Double aggregateDoubleValues(List<Double> doubleValues);

  protected List<Integer> convertValuesToInteger(List<TypedValue> typedValues) throws IllegalArgumentException {
    List<Integer> intValues = new ArrayList<Integer>();
    for (TypedValue typedValue : typedValues) {

      if (ValueType.INTEGER.equals(typedValue.getType())) {
        intValues.add((Integer) typedValue.getValue());

      } else if (typedValue.getType() == null) {
        // check if it is an integer

        Object value = typedValue.getValue();
        if (value instanceof Integer) {
          intValues.add((Integer) value);

        } else {
          throw new IllegalArgumentException();
        }

      } else {
        // reject other typed values
        throw new IllegalArgumentException();
      }

    }
    return intValues;
  }

  protected List<Long> convertValuesToLong(List<TypedValue> typedValues) throws IllegalArgumentException {
    List<Long> longValues = new ArrayList<Long>();
    for (TypedValue typedValue : typedValues) {

      if (ValueType.LONG.equals(typedValue.getType())) {
        longValues.add((Long) typedValue.getValue());

      } else if (typedValue.getType() == null) {
        // check if it is a long or a string of a number

        Object value = typedValue.getValue();
        if (value instanceof Long) {
          longValues.add((Long) value);

        } else {
          Long longValue = Long.valueOf(value.toString());
          longValues.add(longValue);
        }

      } else {
        // reject other typed values
        throw new IllegalArgumentException();
      }

    }
    return longValues;
  }


  protected List<Double> convertValuesToDouble(List<TypedValue> typedValues) throws IllegalArgumentException {
    List<Double> doubleValues = new ArrayList<Double>();
    for (TypedValue typedValue : typedValues) {

      if (ValueType.DOUBLE.equals(typedValue.getType())) {
        doubleValues.add((Double) typedValue.getValue());

      } else if (typedValue.getType() == null) {
        // check if it is a double or a string of a decimal number

        Object value = typedValue.getValue();
        if (value instanceof Double) {
          doubleValues.add((Double) value);

        } else {
          Double doubleValue = Double.valueOf(value.toString());
          doubleValues.add(doubleValue);
        }

      } else {
        // reject other typed values
        throw new IllegalArgumentException();
      }

    }
    return doubleValues;
  }

}
