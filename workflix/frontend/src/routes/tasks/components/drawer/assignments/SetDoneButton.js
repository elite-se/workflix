// @flow

import React from 'react'
import type { UserType } from '../../../../../modules/datatypes/User'
import type { TaskAssignmentType, TaskType } from '../../../../../modules/datatypes/Task'
import { Button } from '@blueprintjs/core'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'
import ProcessApi from '../../../../../modules/api/ProcessApi'
import { toastifyError } from '../../../../../modules/common/toastifyError'

type PropsType = {|
  task: TaskType,
  onAssignmentsChanged: (TaskAssignmentType[]) => void,
  user: UserType
|}

class SetDoneButton extends React.Component<PropsType, { loading: boolean }> {
  state = { loading: false }

  onClick = () => {
    this.setState({ loading: true })
    const { task, user, onAssignmentsChanged } = this.props
    new ProcessApi().markAsDone(task.id, user.id)
      .then(() => {
        this.setState({ loading: false })
        onAssignmentsChanged(task.assignments.map(ass => ass.assigneeId === user.id ? {
          ...ass,
          closed: true,
          doneAt: new Date()
        } : ass))
      })
      .catch(err => {
        this.setState({ loading: false })
        toastifyError(err)
      })
  }

  render () {
    return <Button intent={Intent.SUCCESS} fill loading={this.state.loading} onClick={this.onClick}
            text='I am done' icon='tick' style={{ margin: '5px 0' }}/>
  }
}

export default SetDoneButton
