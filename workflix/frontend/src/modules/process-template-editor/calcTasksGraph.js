// @flow

import type { TaskTemplateType, TaskType } from '../datatypes/Task'
import { calcGraph } from './graph-utils'

const calcTasksGraph = (tasks: TaskType[], taskTemplates: Map<number, TaskTemplateType>) => {
  const preparedTasks = tasks.map(task => {
    const template = taskTemplates.get(task.taskTemplateId)
    if (!template) {
      return null
    }
    const predecessors = template.predecessors
      .map(pred => tasks.find(pTask => pTask.taskTemplateId === pred))
      .filter(Boolean).map(pred => pred.id)
    return {
      id: task.id,
      task,
      name: template.name,
      estimatedDuration: template.estimatedDuration,
      predecessors
    }
  }).filter(Boolean)
  return calcGraph<*, *>(preparedTasks)
}

export default calcTasksGraph
