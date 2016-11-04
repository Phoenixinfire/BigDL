/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intel.analytics.sparkdl.nn

import com.intel.analytics.sparkdl.tensor.Tensor
import com.intel.analytics.sparkdl.tensor.TensorNumericMath.TensorNumeric
import com.intel.analytics.sparkdl.utils.Table

import scala.reflect.ClassTag


class CSubTable[T: ClassTag]()(
  implicit ev: TensorNumeric[T]) extends Module[Table, Tensor[T], T]{

  override def updateOutput(input: Table): Tensor[T] = {
    output.resizeAs(input(1)).copy(input(1))
    output.add(ev.fromType(-1), input(2))
    output
  }

  override def updateGradInput(input: Table, gradOutput: Tensor[T]) : Table = {
    if (!gradInput.contains(1)) gradInput.insert(1, Tensor[T]())
    if (!gradInput.contains(2)) gradInput.insert(2, Tensor[T]())

    gradInput[Tensor[T]](1).resizeAs(input(1)).copy(gradOutput)
    gradInput[Tensor[T]](2).resizeAs(input(2)).copy(gradOutput).mul(ev.fromType(-1))
    gradInput
  }

  override def toString(): String = {
    s"nn.CSubTable"
  }
}
