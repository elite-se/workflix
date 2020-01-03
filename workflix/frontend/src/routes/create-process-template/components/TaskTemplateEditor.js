// @flow

import React from 'react'
import { H3, H4, InputGroup } from '@blueprintjs/core'
import type { TaskTemplateType } from '../../../modules/datatypes/Task'
import { difference } from 'lodash'
import PredecessorSelect from './PredecessorSelect'
import SuccessorSelect from './SuccessorSelect'

type PropsType = {
  task: TaskTemplateType,
  onChange: (task: TaskTemplateType) => void,
  allTasks: TaskTemplateType[]
}

const findAncestors = (task: TaskTemplateType, allTasks: TaskTemplateType[]) => {
  return [
    task,
    ...task.predecessors
      .map(predId => allTasks.find(_task => _task.id === predId)).filter(Boolean)
      .flatMap(_task => findAncestors(_task, allTasks))
  ]
}

const findDescendants = (task: TaskTemplateType, allTasks: TaskTemplateType[]) => {
  return [
    task,
    ...allTasks.filter(_task => _task.predecessors.find(id => id === task.id))
      .flatMap(_task => findDescendants(_task, allTasks))
  ]
}

class TaskTemplateEditor extends React.Component<PropsType> {
  onTitleChange = (event: SyntheticInputEvent<HTMLInputElement>) => {
    this.props.onChange({
      ...this.props.task,
      name: event.target.value
    })
  }

  onDurationChange = (event: SyntheticInputEvent<HTMLInputElement>) => {
    this.props.onChange({
      ...this.props.task,
      estimatedDuration: Number(event.target.value)
    })
  }

  render () {
    const { task, allTasks, onChange } = this.props

    const possiblePreds = difference(allTasks, findDescendants(task, allTasks))
    const possibleSuccs = difference(allTasks, findAncestors(task, allTasks))
    const succs = allTasks.filter(_task => _task.predecessors.indexOf(task.id) >= 0)

    return <div style={{
      margin: '10px',
      display: 'flex',
      alignItems: 'stretch',
      flexDirection: 'column',
      flex: 1
    }}>
      <H3>Edit Task Template</H3>
      <div>
        <H4>Name:</H4>
        <InputGroup type='text' placeholder='Name...'
                    value={this.props.task.name}
                    fill
                    onChange={this.onTitleChange}/>
      </div>
      <div>
        <H4>Duration:</H4>
        <InputGroup type='number' placeholder='Duration...'
                    value={this.props.task.estimatedDuration}
                    fill
                    onChange={this.onDurationChange} min={0.1} step={0.1}/>
      </div>
      <div>
        <H4>Predecessor tasks:</H4>
        <PredecessorSelect allTasks={allTasks} onChange={onChange} possiblePreds={possiblePreds} task={task}/>
      </div>
      <div>
        <H4>Successor tasks:</H4>
        <SuccessorSelect allTasks={allTasks} succs={succs} onChange={onChange} possibleSuccs={possibleSuccs}
                         task={task}/>
      </div>
    </div>
  }
}

export default TaskTemplateEditor
