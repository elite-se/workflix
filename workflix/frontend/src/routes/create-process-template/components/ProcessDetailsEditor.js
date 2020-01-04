// @flow

import React from 'react'
import { InputGroup, Label, NumericInput } from '@blueprintjs/core'
import AutoSizeTextArea from '../../../modules/common/AutoSizeTextArea'
import UserSelect from './UserSelect'
import type { UserType } from '../../../modules/datatypes/User'

const handleStringChange = (handler: string => void) =>
  (event: SyntheticInputEvent<HTMLInputElement>) => handler(event.target.value)

type PropsType = {
  onTitleChange: (title: string) => void,
  title: string,
  onDescriptionChange: (description: string) => void,
  description: string,
  onDurationLimitChange: (durationLimit: ?number) => void,
  durationLimit: ?number,
  users: Map<string, UserType>,
  owner: ?UserType,
  onOwnerChange: (owner: UserType) => void
}

class ProcessDetailsEditor extends React.Component<PropsType> {
  render () {
    const {
      onTitleChange,
      title,
      onDescriptionChange,
      description,
      onDurationLimitChange,
      durationLimit,
      users,
      owner,
      onOwnerChange
    } = this.props

    return <>
      <div style={{ display: 'flex' }}>
        <Label>
          Title
          <InputGroup large style={{ minWidth: '500px' }} onChange={handleStringChange(onTitleChange)} value={title}
                      placeholder='Add process template title...'/>
        </Label>
      </div>
      <div style={{
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-between'
      }}>
        <div style={{
          display: 'flex',
          flexDirection: 'column',
          width: '30%'
        }}>
          <Label>Description
            <AutoSizeTextArea
              style={{ resize: 'none' }} minRows={4} className='bp3-fill'
              onChange={handleStringChange(onDescriptionChange)} value={description}
              placeholder={'Add description...\n\nWhat is this process about?\nWhen should it be initiated?'}/>
          </Label>
        </div>
        <div style={{
          display: 'flex',
          flexDirection: 'column',
          width: '30%'
        }}>
          <Label>Duration limit
            <NumericInput onValueChange={onDurationLimitChange} min={0.1} stepSize={0.1}
                          value={durationLimit !== null ? durationLimit : ''} fill/>
          </Label>
          <Label>Owner
            <UserSelect users={Array.from(users.values())} activeItem={owner} onItemSelect={onOwnerChange}/>
          </Label>
        </div>
        <div style={{ width: '30%' }}/>
      </div>
    </>
  }
}

export default ProcessDetailsEditor
