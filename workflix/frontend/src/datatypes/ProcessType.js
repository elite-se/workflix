// @flow

import type TaskType from './TaskType'
import type ProcessMasterDataType from './ProcessMasterDataType'

type ProcessType = {
  masterData: ProcessMasterDataType,
  tasks: Array<TaskType>
}
export default ProcessType
