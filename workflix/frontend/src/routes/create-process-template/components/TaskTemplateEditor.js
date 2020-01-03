// @flow

import React from 'react'
import { Colors, H3, H4, InputGroup } from '@blueprintjs/core'
import type { TaskTemplateType } from '../../../datatypes/TaskType'
import { difference } from 'lodash'
import PredecessorSelect from './PredecessorSelect'

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

    return <div style={{
      paddingTop: '10px',
      marginTop: '10px',
      borderTop: `1px ${Colors.GRAY1} solid`,
      display: 'flex',
      alignItems: 'center',
      flexDirection: 'column'
    }}>
      <H3>Edit Task Template</H3>
      <div>
        <H4>Name:</H4>
        <InputGroup type='text' placeholder='Name...'
                    value={this.props.task.name}
                    onChange={this.onTitleChange}/>
      </div>
      <div>
        <H4>Duration:</H4>
        <InputGroup type='number' placeholder='Duration...'
                    value={this.props.task.estimatedDuration}
                    onChange={this.onDurationChange} min={0.1} step={0.1}/>
      </div>
      <div>
        <H4>Predecessor tasks:</H4>
        <PredecessorSelect allTasks={allTasks} onChange={onChange} possiblePreds={possiblePreds} task={task}/>
      </div>
    </div>
  }
}

export default TaskTemplateEditor
