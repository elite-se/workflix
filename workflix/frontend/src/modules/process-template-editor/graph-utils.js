// @flow

import { sortBy, times } from 'lodash'

export type GraphDataType<N: number | ?number> = { id: number, predecessors: number[], estimatedDuration: N }

export type ProcessedNodeType<N: number | ?number, T: GraphDataType<N>> = {|
  data: T,
  id: number,
  critical: boolean,
  startDate: number, /* earliest possible start date */
  estimatedDuration: number,
  endDate: number /* earliest possible end date */
|}

export function calcGraph<N: number | ?number, T: GraphDataType<N>> (
  tasks: T[]
): ProcessedNodeType<N, T>[] {
  const nodes = tasks.map(task => ({
    data: task,
    id: task.id,
    critical: false,
    startDate: 0,
    estimatedDuration: task.estimatedDuration || 1,
    endDate: task.estimatedDuration || 1
  }))

  times(nodes.length - 1, () => { // Bellman-Ford
    for (const node of nodes) {
      node.startDate = Math.max(
        0,
        ...(node.data.predecessors.map(id => nodes.find(x => x.id === id)?.endDate || 0))
      )
      node.endDate = node.startDate + node.estimatedDuration
    }
  })

  const setCriticalPath = (task: ProcessedNodeType<N, T>) => {
    task.critical = true
    task.data.predecessors
      .map(id => nodes.find(_task => _task.id === id)).filter(Boolean)
      .filter(_task => _task.endDate === task.startDate)
      .forEach(setCriticalPath)
  }

  const lastEndDate = Math.max(...nodes.map(node => node.endDate))
  nodes.filter(node => node.endDate === lastEndDate).forEach(setCriticalPath)

  return sortBy(nodes, node => node.startDate, node => node.endDate)
}
