import { useState, useEffect } from 'react';
import PropTypes from 'prop-types';

export default function Profile({ userData }) {

    Profile.propTypes = {
        userData: PropTypes.shape({
            id: PropTypes.string.isRequired,
            firstName: PropTypes.string,
        }).isRequired,
    };

    const [profile, setProfile] = useState({});
    const [editableProfile, setEditableProfile] = useState({});

    useEffect(() => {
        async function fetchProfile() {
            const res = await fetch(`/api/profile/${userData.id}`);
            const data = await res.json();
            setProfile(data.profile);
            setEditableProfile(data.profile);
        }
        fetchProfile();
    }, [userData.id]);

    const handleEditChange = (e) => {
        const { name, value } = e.target;
        setEditableProfile({ ...editableProfile, [name]: value });
    };

    const handleSave = async () => {
        await fetch(`/api/profile/${userData.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(editableProfile),
        });
        setProfile(editableProfile);
    };

    return (
        <div className="profile">
            <h2>{profile.name}</h2>
            <p>Email: {profile.email}</p>
            <textarea
                name="note"
                value={editableProfile.note || ""}
                onChange={handleEditChange}
            />
            <button onClick={handleSave}>Save</button>
        </div>
    );
}
