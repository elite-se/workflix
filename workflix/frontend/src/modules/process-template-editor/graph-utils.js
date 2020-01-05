// @flow

import { times } from 'lodash'

export type ProcessedNodeType<T> = {|
  data: T,
  id: number,
  critical: boolean,
  startDate: number, /* earliest possible start date */
  endDate: number /* earliest possible end date */
|}

export function calcGraph<T: { id: number, predecessors: number[], estimatedDuration: ?number }> (
  tasks: T[]
): ProcessedNodeType<T>[] {
  const nodes = tasks.map(node => ({
    data: node,
    id: node.id,
    critical: false,
    startDate: 0,
    endDate: node.estimatedDuration || 1
  }))

  times(nodes.length - 1, () => { // Bellman-Ford
    for (const node of nodes) {
      node.startDate = Math.max(
        0,
        ...(node.data.predecessors.map(id => nodes.find(x => x.id === id)?.endDate || 0))
      )
      node.endDate = node.startDate + (node.data.estimatedDuration || 1)
    }
  })

  const setCriticalPath = (task: ProcessedNodeType<T>) => {
    task.critical = true
    task.data.predecessors
      .map(id => nodes.find(_task => _task.id === id)).filter(Boolean)
      .filter(_task => _task.endDate === task.startDate)
      .forEach(setCriticalPath)
  }

  const lastEndDate = Math.max(...nodes.map(node => node.endDate))
  nodes.filter(node => node.endDate === lastEndDate).forEach(setCriticalPath)

  return nodes.sort((node1, node2) => node1.startDate - node2.startDate)
}
