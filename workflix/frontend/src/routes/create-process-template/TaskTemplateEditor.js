// @flow

import React from 'react'
import { Colors, H3, H4 } from '@blueprintjs/core'
import type { NodeType } from './CreateProcessTemplate'

type PropsType = {
  task: NodeType,
  onChange: (task: NodeType) => void
}

class TaskTemplateEditor extends React.Component<PropsType> {
  onTitleChange = (event: SyntheticInputEvent<HTMLInputElement>) => {
    this.props.onChange({
      ...this.props.task,
      title: event.target.value
    })
  }

  onDurationChange = (event: SyntheticInputEvent<HTMLInputElement>) => {
    this.props.onChange({
      ...this.props.task,
      duration: Number(event.target.value)
    })
  }

  render () {
    return <div style={{
      paddingTop: '10px',
      marginTop: '10px',
      borderTop: `1px ${Colors.GRAY1} solid`,
      display: 'flex',
      alignItems: 'center',
      flexDirection: 'column'
    }}>
      <H3>Edit Task Template</H3>
      <div><H4>Title:</H4> <input type='text' className='bp3-input' placeholder='Title...'
                                  value={this.props.task.title}
                                  onChange={this.onTitleChange} /></div>
      <div><H4>Duration:</H4> <input type='number' className='bp3-input' placeholder='Duration...'
                                     value={this.props.task.duration}
                                     onChange={this.onDurationChange} min={0.1} step={0.1} /></div>
    </div>
  }
}

export default TaskTemplateEditor
